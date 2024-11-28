create database educa;
use educa;

CREATE TABLE usuario (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    senha VARCHAR(64) NOT NULL
);

CREATE TABLE pessoa (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(15),
    tipo ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO') NOT NULL,
    FOREIGN KEY (login) REFERENCES usuario(login) ON DELETE CASCADE
);

CREATE TABLE aluno (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL UNIQUE,
    turma VARCHAR(50),
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE
);

CREATE TABLE professor (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    numero_funcionario VARCHAR(20) NOT NULL UNIQUE,
    funcao VARCHAR(50),
    turmas TEXT,
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE
);

CREATE TABLE funcionario (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    numero_funcionario VARCHAR(20) NOT NULL UNIQUE,
    funcao VARCHAR(50),
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE
);

CREATE TABLE boletim (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login_aluno VARCHAR(50) NOT NULL,
    materia VARCHAR(100) NOT NULL,
    nota DECIMAL(5, 2) NOT NULL,
    presenca DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (login_aluno) REFERENCES aluno(login) ON DELETE CASCADE
);

INSERT INTO usuario (login, senha) VALUES ('aluno123', SHA2('123', 256));

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo) 
VALUES ('aluno123', 'João Silva', '123.456.789-00', '2005-06-15', '(11) 91234-5678', 'ALUNO');

INSERT INTO aluno (login, matricula, turma, ausencias) 
VALUES ('aluno123', '2023MATRICULA123', '3º Ano B', 2);

SELECT * FROM usuario;
SELECT * FROM pessoa;
SELECT * FROM aluno;