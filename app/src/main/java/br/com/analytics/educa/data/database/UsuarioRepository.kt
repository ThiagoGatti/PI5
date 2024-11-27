package br.com.analytics.educa.data.database

class UsuarioRepository(private val dbHelper: DatabaseHelper) {

    fun inserirUsuario(login: String, senha: String): Boolean {
        val connection = dbHelper.connect() ?: return false
        val sql = "INSERT INTO usuario (login, senha) VALUES (?, ?)"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            statement.setString(2, senha)
            statement.executeUpdate()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection.close()
        }
    }

    fun autenticarUsuario(login: String, senha: String): Boolean {
        val connection = dbHelper.connect() ?: return false
        val sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, login)
            statement.setString(2, senha)
            val resultSet = statement.executeQuery()
            resultSet.next()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection.close()
        }
    }
}
