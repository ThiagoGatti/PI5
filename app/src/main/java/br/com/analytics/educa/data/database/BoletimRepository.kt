package br.com.analytics.educa.data.database

class BoletimRepository(private val dbHelper: DatabaseHelper) {

    fun inserirBoletim(
        loginAluno: String,
        materia: String,
        nota: Double,
        presenca: Double
    ): Boolean {
        val connection = dbHelper.connect() ?: return false
        val sql =
            "INSERT INTO boletim (login_aluno, materia, nota, presenca) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE nota = ?, presenca = ?"
        return try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, loginAluno)
            statement.setString(2, materia)
            statement.setDouble(3, nota)
            statement.setDouble(4, presenca)
            statement.setDouble(5, nota)
            statement.setDouble(6, presenca)
            statement.executeUpdate()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection.close()
        }
    }

    fun buscarBoletim(loginAluno: String): List<Map<String, String>> {
        val connection = dbHelper.connect() ?: return emptyList()
        val sql = "SELECT * FROM boletim WHERE login_aluno = ?"
        val boletins = mutableListOf<Map<String, String>>()
        try {
            val statement = connection.prepareStatement(sql)
            statement.setString(1, loginAluno)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                boletins.add(
                    mapOf(
                        "materia" to resultSet.getString("materia"),
                        "nota" to resultSet.getDouble("nota").toString(),
                        "presenca" to resultSet.getDouble("presenca").toString()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
        return boletins
    }
}