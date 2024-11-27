package br.com.analytics.educa.data.database

class PessoaRepository(private val dbHelper: DatabaseHelper) {

    fun inserirPessoa(
        login: String,
        nome: String,
        cpf: String,
        dataNascimento: String,
        telefone: String?,
        tipo: String
    ): Boolean {
        val connection = dbHelper.connect() ?: return false
        val sql =
            "INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo) VALUES (?, ?, ?, ?, ?, ?)"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            statement.setString(2, nome)
            statement.setString(3, cpf)
            statement.setString(4, dataNascimento)
            statement.setString(5, telefone)
            statement.setString(6, tipo)
            statement.executeUpdate()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection.close()
        }
    }

    fun buscarPessoa(login: String): Map<String, String>? {
        val connection = dbHelper.connect() ?: return null
        val sql = "SELECT * FROM pessoa WHERE login = ?"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                mapOf(
                    "login" to resultSet.getString("login"),
                    "nome" to resultSet.getString("nome"),
                    "cpf" to resultSet.getString("cpf"),
                    "dataNascimento" to resultSet.getDate("data_nascimento").toString(),
                    "telefone" to resultSet.getString("telefone"),
                    "tipo" to resultSet.getString("tipo")
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection.close()
        }
    }
}
