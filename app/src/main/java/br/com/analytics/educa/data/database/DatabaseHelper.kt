package br.com.analytics.educa.data.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class DatabaseHelper {
    private val url = "jdbc:mysql://10.0.2.2:3306/educa"
    private val user = "root"
    private val password = ""

    fun connect(): Connection? {
        return try {
            DriverManager.getConnection(url, user, password)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}