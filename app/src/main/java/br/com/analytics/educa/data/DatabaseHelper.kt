package br.com.analytics.educa.data

import android.content.Context
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class DatabaseHelper(private val context: Context) {

    private val connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=EducaAnalytics;user=Thiago;password=1709"

    fun connect(): Connection? {
        var connection: Connection? = null
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionUrl)
        } catch (e: SQLException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return connection
    }

    fun executeQuery(query: String): ResultSet? {
        var resultSet: ResultSet? = null
        try {
            val connection = connect()
            val statement = connection?.createStatement()
            resultSet = statement?.executeQuery(query)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return resultSet
    }

    fun closeConnection(connection: Connection?) {
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}