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

if ($method === 'GET' && isset($_GET['action'])) {
    $action = $_GET['action'];

    if ($action === 'getAnsweredForms' && isset($_GET['userType']) && isset($_GET['login'])) {
        $userType = $_GET['userType'];
        $login = $_GET['login'];

        $stmt = $conn->prepare("SELECT nome_formulario FROM respostas WHERE login_usuario = ? AND tipo_usuario = ?");
        $stmt->bind_param("ss", $login, $userType);
        $stmt->execute();
        $result = $stmt->get_result();

        $forms = [];
        while ($row = $result->fetch_assoc()) {
            $forms[] = $row['nome_formulario'];
        }

        echo json_encode($forms);
        exit;
    } elseif ($action === 'getTurmas') {
        $stmt = $conn->prepare("SELECT sigla FROM turmas");
        $stmt->execute();
        $result = $stmt->get_result();

        $turmas = [];
        while ($row = $result->fetch_assoc()) {
            $turmas[] = $row['sigla'];
        }

        echo json_encode($turmas);
        exit;
    } elseif ($action === 'getUsersByTurma' && isset($_GET['turma'])) {
        $turma = $_GET['turma'];

        $stmt = $conn->prepare("
            SELECT p.login, p.nome, p.telefone, p.tipo
            FROM pessoa p
            INNER JOIN aluno a ON p.login = a.login
            WHERE a.turma = ?
        ");
        $stmt->bind_param("s", $turma);
        $stmt->execute();
        $result = $stmt->get_result();

        $users = [];
        while ($row = $result->fetch_assoc()) {
            $users[] = [
                "login" => $row['login'],
                "name" => $row['nome'],
                "phone" => $row['telefone'],
                "type" => $row['tipo']
            ];
        }

        echo json_encode($users);
        exit;
    } elseif ($action === 'getUsersByType' && isset($_GET['type'])) {
        $type = $_GET['type'];
        $stmt = $conn->prepare("
            SELECT login, nome, telefone, tipo 
            FROM pessoa 
            WHERE tipo = ?
        ");
        $stmt->bind_param("s", $type);
        $stmt->execute();
        $result = $stmt->get_result();
    
        $users = [];
        while ($row = $result->fetch_assoc()) {
            $users[] = [
                "login" => $row['login'],
                "name" => $row['nome'],
                "phone" => $row['telefone'],
                "type" => $row['tipo']
            ];
        }
    
        echo json_encode($users);
        exit;
    } elseif ($action === 'getBoletim' && isset($_GET['login'])) {
        $login = $_GET['login'];
        
        $stmt = $conn->prepare("
            SELECT materia, nota, presenca
            FROM boletim
            WHERE login_aluno = ?
        ");
        $stmt->bind_param("s", $login);
        $stmt->execute();
        $result = $stmt->get_result();
        
        $boletim = [];
        while ($row = $result->fetch_assoc()) {
            $boletim[] = [
                "materia" => $row['materia'],
                "nota" => round($row['nota'], 2),
                "presenca" => $row['presenca']
            ];
        }
        
        echo json_encode($boletim);
        exit;
    } elseif ($action === 'schoolPerformance' && isset($_GET['login'])) {
        $login = $_GET['login'];
    
        $stmt = $conn->prepare("
            SELECT escola.nome AS nome_escola, escola.id AS id_escola
            FROM pessoa
            INNER JOIN escola ON pessoa.id_escola = escola.id
            WHERE pessoa.login = ?
        ");
        $stmt->bind_param("s", $login);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows === 0) {
            http_response_code(404);
            echo json_encode(["success" => false, "message" => "Usuário ou escola não encontrados"]);
            exit;
        }
    
        $row = $result->fetch_assoc();
        $nomeEscola = $row['nome_escola'];
        $idEscola = $row['id_escola'];
    
        $stmt = $conn->prepare("
            SELECT AVG(boletim.nota) AS media_nota
            FROM boletim
            INNER JOIN aluno ON boletim.login_aluno = aluno.login
            INNER JOIN pessoa ON aluno.login = pessoa.login
            WHERE pessoa.id_escola = ?
        ");
        $stmt->bind_param("i", $idEscola);
        $stmt->execute();
        $result = $stmt->get_result();
    
        $mediaNotas = 0;
        if ($result->num_rows > 0) {
            $mediaNotas = $result->fetch_assoc()['media_nota'];
        }
    
        echo json_encode([
            "nome_escola" => $nomeEscola,
            "media_nota" => round($mediaNotas, 1)
        ]);
        exit;
    } elseif ($method === 'GET' && $action === 'getResponsesBySchool') {
        $login = $_GET['login'] ?? null;
    
        if (!$login) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Login do usuário é obrigatório"]);
            exit;
        }
    
        $stmt = $conn->prepare("
            SELECT id_escola 
            FROM pessoa 
            WHERE login = ?
        ");
        $stmt->bind_param("s", $login);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows === 0) {
            http_response_code(404);
            echo json_encode(["success" => false, "message" => "Usuário não encontrado"]);
            exit;
        }
    
        $id_escola = $result->fetch_assoc()['id_escola'];
    
        $stmt = $conn->prepare("
            SELECT 
                pessoa.tipo AS tipo_usuario,
                respostas.nome_formulario,
                respostas.q1, respostas.q2, respostas.q3, respostas.q4, respostas.q5,
                respostas.q6, respostas.q7, respostas.q8, respostas.q9, respostas.q10,
                respostas.q11, respostas.q12, respostas.q13, respostas.q14, respostas.q15
            FROM respostas
            INNER JOIN pessoa ON respostas.login_usuario = pessoa.login
            WHERE pessoa.id_escola = ?
        ");
        $stmt->bind_param("i", $id_escola);
        $stmt->execute();
        $result = $stmt->get_result();
    
        $responses = [];
        while ($row = $result->fetch_assoc()) {
            $filteredResponses = [];
            foreach ($row as $question => $value) {
                if (!in_array($question, ['nome_formulario', 'tipo_usuario']) && $value > 0) {
                    $filteredResponses[$question] = $value;
                }
            }
            if (!empty($filteredResponses)) {
                $responses[] = [
                    'tipo_usuario' => $row['tipo_usuario'],
                    'nome_formulario' => $row['nome_formulario'],
                    'respostas' => $filteredResponses
                ];
            }
        }
    
        echo json_encode($responses);
        exit;
    }
     else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Ação inválida ou parâmetros ausentes"]);
        exit;
    }
}

if ($method === 'POST') {
    $input = json_decode(file_get_contents("php://input"), true);
    $action = $input['action'] ?? null;

    if ($action === 'saveAnswers') {
        $userType = $input['userType'] ?? null;
        $formName = $input['formName'] ?? null;
        $login = $input['username'] ?? null;
        $answers = $input['answers'] ?? null;

        if (!$userType || !$formName || !$login || !$answers) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Dados incompletos"]);
            exit;
        }

        $columns = implode(", ", array_map(fn($q) => "$q = ?", array_keys($answers)));
        $sql = "INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, " . implode(", ", array_keys($answers)) . ")
                VALUES (?, ?, ?, " . str_repeat("?, ", count($answers) - 1) . "?)
                ON DUPLICATE KEY UPDATE $columns";

        $stmt = $conn->prepare($sql);
        $params = array_merge([$login, $userType, $formName], array_values($answers), array_values($answers));
        $stmt->bind_param(str_repeat("s", 3) . str_repeat("i", count($answers) * 2), ...$params);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Respostas salvas com sucesso."]);
        } else {
            echo json_encode(["success" => false, "message" => "Erro ao salvar respostas: " . $stmt->error]);
        }

        $stmt->close();
    } elseif ($action === 'removeUser') {
        $login = $input['login'] ?? null;
    
        if (!$login) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Login é obrigatório"]);
            exit;
        }
    
        $stmt = $conn->prepare("DELETE FROM usuario WHERE login = ?");
        $stmt->bind_param("s", $login);
    
        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Usuário e dependências removidos com sucesso"]);
        } else {
            echo json_encode(["success" => false, "message" => "Erro ao remover o usuário"]);
        }
    
        $stmt->close();
    } elseif ($action === 'login') {
        $login = $input['username'] ?? null;
        $senha = $input['password'] ?? null;

        if (!$login || !$senha) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Login e senha são obrigatórios"]);
            exit;
        }

        $stmt = $conn->prepare("
            SELECT pessoa.nome, pessoa.tipo 
            FROM usuario 
            INNER JOIN pessoa ON usuario.login = pessoa.login
            WHERE usuario.login = ? AND usuario.senha = SHA2(?, 256)
        ");
        $stmt->bind_param("ss", $login, $senha);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode([
                "success" => true,
                "nome" => $row['nome'],
                "tipo" => $row['tipo']
            ]);
        } else {
            http_response_code(401);
            echo json_encode(["success" => false, "message" => "Login ou senha incorretos"]);
        }

        $stmt->close();
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Ação não especificada ou inválida"]);
    }
} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Método não permitido"]);
}

$conn->close();
?>