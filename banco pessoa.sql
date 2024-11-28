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
    tipo ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO', 'DIRETOR') NOT NULL,
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

CREATE TABLE respostas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login_usuario VARCHAR(50) NOT NULL,
    tipo_usuario ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO') NOT NULL,
    nome_formulario VARCHAR(100) NOT NULL,
    q1 INT DEFAULT 0,
    q2 INT DEFAULT 0,
    q3 INT DEFAULT 0,
    q4 INT DEFAULT 0,
    q5 INT DEFAULT 0,
    q6 INT DEFAULT 0,
    q7 INT DEFAULT 0,
    q8 INT DEFAULT 0,
    q9 INT DEFAULT 0,
    q10 INT DEFAULT 0,
    q11 INT DEFAULT 0,
    q12 INT DEFAULT 0,
    q13 INT DEFAULT 0,
    q14 INT DEFAULT 0,
    q15 INT DEFAULT 0,
    FOREIGN KEY (login_usuario) REFERENCES usuario(login) ON DELETE CASCADE
);

INSERT INTO usuario (login, senha) VALUES ('aluno123', SHA2('123', 256));

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo) 
VALUES ('aluno123', 'João Silva', '123.456.789-00', '2005-06-15', '(11) 91234-5678', 'ALUNO');

INSERT INTO aluno (login, matricula, turma, ausencias) 
VALUES ('aluno123', '2023MATRICULA123', '3º Ano B', 2);

SELECT * FROM usuario;
SELECT * FROM pessoa;
SELECT * FROM aluno;
SELECT * FROM respostas;