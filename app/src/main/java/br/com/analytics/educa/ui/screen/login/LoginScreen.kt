import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.content.Context
import android.util.Base64
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSubmit: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var saveLogin by remember { mutableStateOf(false) }
    var autoLoginAttempted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!autoLoginAttempted) {
            val isSaveEnabled = sharedPreferences.getBoolean("save_login", false)
            if (isSaveEnabled) {
                val savedUsername = sharedPreferences.getString("username", null)
                val savedPassword = sharedPreferences.getString("password", null)
                if (savedUsername != null && savedPassword != null) {
                    val decodedPassword = String(Base64.decode(savedPassword, Base64.DEFAULT))
                    onLoginSubmit(savedUsername, decodedPassword)
                }
            }
            autoLoginAttempted = true
        }
    }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Educa Analytics",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = Color.White) },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = saveLogin,
                        onCheckedChange = {
                            saveLogin = it
                            sharedPreferences.edit()
                                .putBoolean("save_login", it)
                                .apply()
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Salvar login",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {

                        if (saveLogin) {
                            sharedPreferences.edit().apply {
                                putString("username", username)
                                putString(
                                    "password",
                                    Base64.encodeToString(password.toByteArray(), Base64.DEFAULT)
                                )
                                apply()
                            }
                        } else {

                            sharedPreferences.edit().remove("username").remove("password").apply()
                        }
                        onLoginSubmit(username, password)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ícone de login",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Login", color = Color.White)
                    }
                }
            }
        }
    }
}