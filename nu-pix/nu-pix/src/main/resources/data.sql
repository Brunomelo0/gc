-- Cria Tabela Usuario
CREATE TABLE usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE,
    cnpj VARCHAR(14) UNIQUE
);

-- Inserção de Usuários
INSERT INTO usuario (nome, email, telefone, cpf, cnpj) VALUES
('João Silva', 'joao@gmail.com', '+5511987654321', '12345678901', '11122233344455'),
('Maria Oliveira', 'maria@gmail.com', '+5511987654322', '98765432109', '22233344455566'),
('Pedro Santos', 'pedro@gmail.com', '+5511987654323', '45678912301', NULL),
('Ana Souza', 'ana@gmail.com', '+5511987654324', '32165498700', NULL),
('Lucas Almeida', 'lucas@gmail.com', '+5511987654325', '74125896300', NULL),
('Beatriz Lima', 'beatriz@gmail.com', '+5511987654326', '96385274100', NULL),
('Carlos Moreira', 'carlos@gmail.com', '+5511987654327', '15975325800', NULL),
('Fernanda Rocha', 'fernanda@gmail.com', '+5511987654328', '75395145600', NULL),
('Gabriel Nunes', 'gabriel@gmail.com', '+5511987654329', '85236974100', NULL),
('Juliana Mendes', 'juliana@gmail.com', '+5511987654330', '95175385200', NULL),
('Roberto Pereira', 'roberto@gmail.com', '+5511987654331', '96325874100', '33344455566677'),
('Juliana Silva', 'juliana.silva@gmail.com', '+5511987654332', '85296374100', '44455566677788');

-- Cria Tabela Conta Bancária com o campo de limite diário
CREATE TABLE conta_bancaria (
    conta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    usuario_id BIGINT NOT NULL,
    limite_diario DECIMAL(15,2) DEFAULT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
);

-- Inserção de Contas Bancárias com limites diários definidos
INSERT INTO conta_bancaria(numero, saldo, usuario_id, senha, limite_diario) VALUES
('0001-01', 5000.00, 1, 'senha123', 1000.00),
('0001-02', 3000.00, 2, 'senha456', 500.00),
('0001-03', 10000.00, 3, 'senha789', 2000.00),
('0001-04', 8000.00, 4, 'senha321', 1500.00),
('0001-05', 2500.00, 5, 'senha654', 1200.00),
('0001-06', 1500.00, 6, 'senha987', 1000.00),
('0001-07', 6000.00, 7, 'senha111', 3000.00),
('0001-08', 9000.00, 8, 'senha222', 2500.00),
('0001-09', 7000.00, 9, 'senha333', 1800.00),
('0001-10', 2000.00, 10, 'senha444', 800.00),
('0001-11', 5000.00, 11, 'senha555', 1500.00),
('0001-12', 12000.00, 12, 'senha666', 3500.00);

-- Cria Tabela ChavePix
CREATE TABLE chave_pix (
    chave_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    valor VARCHAR(255) NOT NULL,
    conta_id BIGINT NOT NULL,
    suspensa BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (conta_id) REFERENCES conta_bancaria(conta_id) ON DELETE CASCADE
);

-- Inserção de Chaves CPF
INSERT INTO chave_pix (tipo, valor, suspensa, conta_id) VALUES
('CPF', '98765432109', true, 2),
('CPF', '45678912301', true, 3),
('CPF', '32165498700', true, 4),
('CPF', '74125896300', true, 5),
('CPF', '96385274100', true, 6),
('CPF', '15975325800', true, 7),
('CPF', '75395145600', true, 8),
('CPF', '85236974100', true, 9),
('CPF', '95175385200', true, 10);

-- Inserção de Chaves CNPJ
INSERT INTO chave_pix (tipo, valor, conta_id) VALUES
('CNPJ', '11122233344455', 1),
('CNPJ', '22233344455566', 2),
('CNPJ', '33344455566677', 11),
('CNPJ', '44455566677788', 12);

-- Inserção de Chaves Telefone
INSERT INTO chave_pix (tipo, valor, conta_id) VALUES
('TELEFONE', '+5511987654321', 1),
('TELEFONE', '+5511987654322', 2),
('TELEFONE', '+5511987654323', 3),
('TELEFONE', '+5511987654324', 4),
('TELEFONE', '+5511987654325', 5),
('TELEFONE', '+5511987654326', 6),
('TELEFONE', '+5511987654327', 7),
('TELEFONE', '+5511987654328', 8),
('TELEFONE', '+5511987654329', 9),
('TELEFONE', '+5511987654330', 10);

-- Inserção de Chaves E-mail
INSERT INTO chave_pix (tipo, valor, conta_id) VALUES
('EMAIL', 'lucas@gmail.com', 5),
('EMAIL', 'beatriz@gmail.com', 6),
('EMAIL', 'carlos@gmail.com', 7),
('EMAIL', 'fernanda@gmail.com', 8),
('EMAIL', 'gabriel@gmail.com', 9);

-- Inserção de Chaves Aleatórias
INSERT INTO chave_pix (tipo, valor, conta_id) VALUES
('ALEATORIA', 'b4070ad9-05ea-4e44-9914-5e20c33dbdb0', 1),
('ALEATORIA', 'f1d02a91-9e73-4d72-84d8-b9ad1e1e4879', 1),
('ALEATORIA', 'd8c1e3f2-49c3-4d56-b3a6-9c92804e7e41', 3),
('ALEATORIA', 'acde8f01-20c7-4d7f-aad4-03e5e2bb7b62', 4),
('ALEATORIA', '22fe389b-44fa-4f25-9c34-f051c8966d18', 5),
('ALEATORIA', 'bb2ad9a3-b9d3-41d3-b19b-589dd11e1a64', 6),
('ALEATORIA', '2c8ff1dd-0d3e-4c12-b2b4-6ab5e1f44b23', 8),
('ALEATORIA', 'f44bfa76-c9a6-40f1-a9c9-f2bdcfa3e2d0', 9),
('ALEATORIA', 'e4c81e7b-c324-46b6-aade-b1bdf8f1043c', 10),
('ALEATORIA', 'cfd2bc5b-5f3c-4b8a-94bd-a4575f1a8844', 11),
('ALEATORIA', 'd4ebbe56-8e1a-4c64-8ab4-cd8ff354ed69', 12);

-- Criação da Tabela Transacao
CREATE TABLE transacao (
    id_transacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    conta_origem_id BIGINT NOT NULL,
    conta_destino_id BIGINT NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    data DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    FOREIGN KEY (conta_origem_id) REFERENCES conta_bancaria(conta_id) ON DELETE CASCADE,
    FOREIGN KEY (conta_destino_id) REFERENCES conta_bancaria(conta_id) ON DELETE CASCADE,
    CONSTRAINT check_status CHECK (status IN ('PENDENTE', 'CONCLUIDA', 'CANCELADA', 'ESTORNADA'))
);

-- Inserção de Dados na Tabela Transacao
INSERT INTO transacao (conta_origem_id, conta_destino_id, valor, data, status) VALUES
(1, 2, 150.00, '2025-01-01', 'CONCLUIDA'),
(2, 3, 200.00, '2025-01-01', 'CONCLUIDA'),
(3, 4, 500.00, '2025-01-02', 'PENDENTE'),
(4, 5, 100.00, '2025-01-02', 'CANCELADA'),
(5, 6, 300.00, '2025-01-03', 'CONCLUIDA'),
(6, 7, 250.00, '2025-01-03', 'PENDENTE'),
(7, 8, 400.00, '2025-01-04', 'CONCLUIDA'),
(8, 9, 100.00, '2025-01-04', 'PENDENTE'),
(9, 10, 500.00, '2025-01-05', 'CONCLUIDA'),
(10, 11, 200.00, '2025-01-05', 'CANCELADA'),
(11, 12, 1000.00, '2025-01-06', 'CONCLUIDA');