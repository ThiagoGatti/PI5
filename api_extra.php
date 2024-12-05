<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "educa";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Falha na conexão com o banco de dados"]);
    exit;
}

$method = $_SERVER['REQUEST_METHOD'];
try {
if ($method === 'GET' && isset($_GET['action'])) {
    $action = $_GET['action'];

    if ($action === 'getUserDetails' && isset($_GET['login'])) {
        $login = $_GET['login'];
    
        $stmt = $conn->prepare("
            SELECT 
                p.login, p.nome, p.cpf, p.data_nascimento, p.telefone, 
                p.tipo
            FROM pessoa p
            WHERE p.login = ?
        ");
        $stmt->bind_param("s", $login);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows === 0) {
            http_response_code(404);
            echo json_encode(["success" => false, "message" => "Usuário não encontrado"]);
            exit;
        }
    
        $user = $result->fetch_assoc();
        $userCompleto = [
            "login" => $user['login'],
            "name" => $user['nome'],
            "cpf" => $user['cpf'],
            "birthDate" => $user['data_nascimento'],
            "phone" => $user['telefone'],
            "type" => $user['tipo'],
            "components" => []
        ];
    
        if ($user['tipo'] === 'ALUNO') {
            $stmt = $conn->prepare("
                SELECT a.turma
                FROM aluno a
                WHERE a.login = ?
            ");
            $stmt->bind_param("s", $login);
            $stmt->execute();
            $result = $stmt->get_result();
    
            if ($result->num_rows > 0) {
                $aluno = $result->fetch_assoc();
                $userCompleto['components'] = [
                    "turma" => $aluno['turma']
                ];
            }
        } elseif ($user['tipo'] === 'PROFESSOR') {
            $stmt = $conn->prepare("
                SELECT p.materia, p.turmas
                FROM professor p
                WHERE p.login = ?
            ");
            $stmt->bind_param("s", $login);
            $stmt->execute();
            $result = $stmt->get_result();
    
            if ($result->num_rows > 0) {
                $professor = $result->fetch_assoc();
                $userCompleto['components'] = [
                    "materia" => $professor['materia'],
                    "turmas" => json_decode($professor['turmas'])
                ];
            }
        } elseif ($user['tipo'] === 'FUNCIONARIO') {
            $stmt = $conn->prepare("
                SELECT f.funcao
                FROM funcionario f
                WHERE f.login = ?
            ");
            $stmt->bind_param("s", $login);
            $stmt->execute();
            $result = $stmt->get_result();
    
            if ($result->num_rows > 0) {
                $funcionario = $result->fetch_assoc();
                $userCompleto['components'] = [
                    "funcao" => $funcionario['funcao']
                ];
            }
        }
    
        echo json_encode($userCompleto);
        exit;
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Ação inválida ou parâmetros ausentes"]);
        exit;
    }
}

if ($method === 'POST') {
    $input = json_decode(file_get_contents("php://input"), true);
    $action = $input['action'] ?? null;

    if ($action === 'createUserCompleto') {
        $login = $input['login'] ?? null;
        $password = $input['password'] ?? null;
        $name = $input['name'] ?? null;
        $cpf = $input['cpf'] ?? null;
        $birthDate = $input['birthDate'] ?? null;
        $phone = $input['phone'] ?? null;
        $type = $input['type'] ?? null;
        $components = $input['components'] ?? [];
    
        if (!$login || !$password || !$name || !$cpf || !$birthDate || !$phone || !$type) {
            echo json_encode(["success" => false, "message" => "Dados incompletos"]);
            exit;
        }
    
        // Inserir primeiro na tabela `usuario`
        $hashedPassword = hash('sha256', $password);
        $stmt = $conn->prepare("INSERT INTO usuario (login, senha) VALUES (?, ?)");
        $stmt->bind_param("ss", $login, $hashedPassword);
        if (!$stmt->execute()) {
            echo json_encode(["success" => false, "message" => "Erro ao criar na tabela usuario: " . $stmt->error]);
            exit;
        }
    
        // Inserir na tabela `pessoa`
        $stmt = $conn->prepare("INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssss", $login, $name, $cpf, $birthDate, $phone, $type);
        if (!$stmt->execute()) {
            echo json_encode(["success" => false, "message" => "Erro ao criar na tabela pessoa: " . $stmt->error]);
            exit;
        }
    
        // Manipular componentes específicos de acordo com o tipo de usuário
        if ($type === 'ALUNO') {
            $turma = $components['turma'] ?? null;
            if ($turma) {
                // Verificar se a turma já existe
                $stmt = $conn->prepare("SELECT sigla FROM turmas WHERE sigla = ?");
                $stmt->bind_param("s", $turma);
                $stmt->execute();
                $result = $stmt->get_result();
        
                if ($result->num_rows === 0) {
                    // Criar a turma se não existir
                    $materias = json_encode([]); // Ajuste para definir matérias padrão se necessário
                    $stmt = $conn->prepare("INSERT INTO turmas (sigla, materias) VALUES (?, ?)");
                    $stmt->bind_param("ss", $turma, $materias);
        
                    if (!$stmt->execute()) {
                        echo json_encode(["success" => false, "message" => "Erro ao criar turma"]);
                        exit;
                    }
                }
        
                // Adicionar o aluno à turma
                $stmt = $conn->prepare("INSERT INTO aluno (login, turma) VALUES (?, ?) ON DUPLICATE KEY UPDATE turma = VALUES(turma)");
                $stmt->bind_param("ss", $login, $turma);
        
                if (!$stmt->execute()) {
                    echo json_encode(["success" => false, "message" => "Erro ao adicionar aluno à turma"]);
                    exit;
                }
            }
        } elseif ($type === 'PROFESSOR') {
            $materia = $components['materia'] ?? null;
            $turmas = $components['turmas'] ?? [];
            if ($materia && !empty($turmas)) {
                $stmt = $conn->prepare("INSERT INTO professor (login, materia, turmas) VALUES (?, ?, ?)");
                $turmasJson = json_encode($turmas);
                $stmt->bind_param("sss", $login, $materia, $turmasJson);
                $stmt->execute();
            }
        } elseif ($type === 'FUNCIONARIO') {
            $funcao = $components['funcao'] ?? null;
            if ($funcao) {
                $stmt = $conn->prepare("INSERT INTO funcionario (login, funcao) VALUES (?, ?)");
                $stmt->bind_param("ss", $login, $funcao);
                $stmt->execute();
            }
        }
    
        echo json_encode(["success" => true, "message" => "Usuário criado com sucesso"]);
        exit;
    } elseif ($action === 'updateUserCompleto') {
        $login = $input['login'] ?? null;
        $name = $input['name'] ?? null;
        $cpf = $input['cpf'] ?? null;
        $birthDate = $input['birthDate'] ?? null;
        $phone = $input['phone'] ?? null;
        $type = $input['type'] ?? null;
        $password = $input['password'] ?? null;
        $components = $input['components'] ?? [];
    
        if (!$login || !$name || !$cpf || !$birthDate || !$phone || !$type) {
            echo json_encode(["success" => false, "message" => "Dados incompletos para edição"]);
            exit;
        }
    
        // Atualizar informações gerais do usuário na tabela `pessoa`
        $stmt = $conn->prepare("
            UPDATE pessoa 
            SET nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, tipo = ?
            WHERE login = ?
        ");
        $stmt->bind_param("ssssss", $name, $cpf, $birthDate, $phone, $type, $login);
        if (!$stmt->execute()) {
            echo json_encode(["success" => false, "message" => "Erro ao atualizar na tabela pessoa: " . $stmt->error]);
            exit;
        }
    
        // Atualizar senha, se fornecida
        if ($password) {
            $hashedPassword = hash('sha256', $password);
            $stmt = $conn->prepare("UPDATE usuario SET senha = ? WHERE login = ?");
            $stmt->bind_param("ss", $hashedPassword, $login);
            if (!$stmt->execute()) {
                echo json_encode(["success" => false, "message" => "Erro ao atualizar senha: " . $stmt->error]);
                exit;
            }
        }
    
        // Atualizar informações específicas do tipo de usuário
        if ($type === 'ALUNO') {
            $turma = $components['turma'] ?? null;
            if ($turma) {
                $stmt = $conn->prepare("
                    INSERT INTO aluno (login, turma) VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE turma = VALUES(turma)
                ");
                $stmt->bind_param("ss", $login, $turma);
                if (!$stmt->execute()) {
                    echo json_encode(["success" => false, "message" => "Erro ao atualizar informações de aluno"]);
                    exit;
                }
            }
        } elseif ($type === 'PROFESSOR') {
            $materia = $components['materia'] ?? null;
            $turmas = $components['turmas'] ?? [];
            if ($materia && !empty($turmas)) {
                $turmasJson = json_encode($turmas);
                $stmt = $conn->prepare("
                    INSERT INTO professor (login, materia, turmas) VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE materia = VALUES(materia), turmas = VALUES(turmas)
                ");
                $stmt->bind_param("sss", $login, $materia, $turmasJson);
                if (!$stmt->execute()) {
                    echo json_encode(["success" => false, "message" => "Erro ao atualizar informações de professor"]);
                    exit;
                }
            }
        } elseif ($type === 'FUNCIONARIO') {
            $funcao = $components['funcao'] ?? null;
            if ($funcao) {
                $stmt = $conn->prepare("
                    INSERT INTO funcionario (login, funcao) VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE funcao = VALUES(funcao)
                ");
                $stmt->bind_param("ss", $login, $funcao);
                if (!$stmt->execute()) {
                    echo json_encode(["success" => false, "message" => "Erro ao atualizar informações de funcionário"]);
                    exit;
                }
            }
        }
    
        echo json_encode(["success" => true, "message" => "Usuário atualizado com sucesso"]);
        exit;
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Ação não especificada ou inválida"]);
    }
} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Método não permitido"]);
}

}catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => $e->getMessage()]);
}
$conn->close();
?>