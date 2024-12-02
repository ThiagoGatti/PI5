CREATE DATABASE educa;
USE educa;

CREATE TABLE usuario (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    senha VARCHAR(64) NOT NULL
);

CREATE TABLE escola (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco TEXT NOT NULL
);

CREATE TABLE pessoa (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(15),
    tipo ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO', 'DIRETOR') NOT NULL,
    id_escola INT NOT NULL,
    FOREIGN KEY (id_escola) REFERENCES escola(id) ON DELETE CASCADE,
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
    q1 INT DEFAULT -1,
    q2 INT DEFAULT -1,
    q3 INT DEFAULT -1,
    q4 INT DEFAULT -1,
    q5 INT DEFAULT -1,
    q6 INT DEFAULT -1,
    q7 INT DEFAULT -1,
    q8 INT DEFAULT -1,
    q9 INT DEFAULT -1,
    q10 INT DEFAULT -1,
    q11 INT DEFAULT -1,
    q12 INT DEFAULT -1,
    q13 INT DEFAULT -1,
    q14 INT DEFAULT -1,
    q15 INT DEFAULT -1,
    FOREIGN KEY (login_usuario) REFERENCES usuario(login) ON DELETE CASCADE
);

INSERT INTO usuario (login, senha) VALUES ('aluno123', SHA2('123', 256));

INSERT INTO escola (nome, endereco) 
VALUES ('Escola Estadual Modelo', 'Rua Principal, 123, Cidade Exemplo');

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) 
VALUES ('aluno123', 'João Silva', '123.456.789-00', '2005-06-15', '(11) 91234-5678', 'ALUNO', 1);

INSERT INTO aluno (login, matricula, turma, ausencias) 
VALUES ('aluno123', '2023MATRICULA123', '3B', 2);

SELECT * FROM usuario;
SELECT * FROM pessoa;
SELECT * FROM aluno;
SELECT * FROM respostas;

DESCRIBE respostas;

-- Adicionando mais usuários
INSERT INTO usuario (login, senha) VALUES ('aluno456', SHA2('123', 256));
INSERT INTO usuario (login, senha) VALUES ('aluno789', SHA2('123', 256));

-- Adicionando mais escolas
INSERT INTO escola (nome, endereco) 
VALUES ('Escola Municipal Aprender', 'Rua Secundária, 45, Cidade Exemplo'),
       ('Escola Técnica Saber', 'Avenida Central, 321, Cidade Exemplo');

-- Adicionando mais pessoas (alunos)
INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) 
VALUES 
('aluno456', 'Maria Oliveira', '987.654.321-00', '2004-09-10', '(11) 99876-5432', 'ALUNO', 2),
('aluno789', 'Carlos Santos', '654.321.987-00', '2003-11-20', '(11) 91234-8765', 'ALUNO', 3);

-- Adicionando mais alunos
INSERT INTO aluno (login, matricula, turma, ausencias) 
VALUES 
('aluno456', '2023MATRICULA456', '2A', 1),
('aluno789', '2023MATRICULA789', '1C', 3);

-- Adicionando registros ao boletim
INSERT INTO boletim (login_aluno, materia, nota, presenca) 
VALUES 
-- Notas e presenças de João Silva
('aluno123', 'Matemática', 8.5, 95),
('aluno123', 'Português', 7.0, 90),
('aluno123', 'História', 9.0, 98),
('aluno123', 'Ciências', 6.5, 85),

-- Notas e presenças de Maria Oliveira
('aluno456', 'Matemática', 7.5, 88),
('aluno456', 'Português', 8.0, 92),
('aluno456', 'História', 7.8, 89),
('aluno456', 'Ciências', 6.0, 80),

-- Notas e presenças de Carlos Santos
('aluno789', 'Matemática', 9.0, 99),
('aluno789', 'Português', 8.5, 95),
('aluno789', 'História', 7.0, 87),
('aluno789', 'Ciências', 8.2, 93);