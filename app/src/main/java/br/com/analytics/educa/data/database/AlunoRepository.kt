package br.com.analytics.educa.data.database

class AlunoRepository(private val dbHelper: DatabaseHelper) {

    fun inserirAluno(login: String, matricula: String, turma: String?, ausencias: Int = 0):

            Boolean {
        val connection = dbHelper.connect() ?: return false
        val sql = "INSERT INTO aluno (login, matricula, turma, ausencias) VALUES (?, ?, ?, ?)"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            statement.setString(2, matricula)
            statement.setString(3, turma)
            statement.setInt(4, ausencias)
            statement.executeUpdate()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection.close()
        }
    }

    fun buscarAluno(login: String): Map<String, String>? {
        val connection = dbHelper.connect() ?: return null
        val sql = "SELECT * FROM aluno WHERE login = ?"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                mapOf(
                    "login" to resultSet.getString("login"),
                    "matricula" to resultSet.getString("matricula"),
                    "turma" to resultSet.getString("turma"),
                    "ausencias" to resultSet.getInt("ausencias").toString()
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