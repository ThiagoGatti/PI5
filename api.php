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
    } elseif ($action === 'schoolPerformance') {
        $stmt = $conn->prepare("
            SELECT 
                AVG(nota) AS media_nota,
                AVG(presenca) AS media_presenca,
                materia
            FROM boletim
            GROUP BY materia
        ");
        $stmt->execute();
        $result = $stmt->get_result();

        $performance = [];
        while ($row = $result->fetch_assoc()) {
            $performance[] = $row;
        }

        echo json_encode($performance);
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