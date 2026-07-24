package com.example.secretvault

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var sharedPrefs: SharedPreferences
    private var tapCount = 0
    private var lastTapTime: Long = 0
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("SecretVaultPrefs", Context.MODE_PRIVATE)

        val tvDisplay = findViewById<TextView>(R.id.tvDisplay)
        val ivSettings = findViewById<ImageView>(R.id.ivSettings)

        // 1. Check if first time opening the app
        val savedPassword = sharedPrefs.getString("VAULT_PASSWORD", null)
        if (savedPassword.isNullOrEmpty()) {
            tvDisplay.post {
                showFirstTimeSetupDialog()
            }
        }

        // 2. Secret 5-Tap on Settings Icon
        ivSettings.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTapTime < 600) {
                tapCount++
            } else {
                tapCount = 1
            }
            lastTapTime = currentTime

            if (tapCount == 5) {
                showResetPasswordDialog()
                tapCount = 0
            }
        }

        // 3. Calculator Buttons Mapping
        val buttons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to ".", R.id.btnPercent to "%",
            R.id.btnDivide to "÷", R.id.btnMultiply to "×",
            R.id.btnMinus to "-", R.id.btnPlus to "+"
        )

        for ((id, value) in buttons) {
            findViewById<Button>(id).setOnClickListener {
                if (currentInput == "0" && value != ".") currentInput = ""
                currentInput += value
                tvDisplay.text = currentInput
            }
        }

        // Brackets Button ( )
        findViewById<Button>(R.id.btnBracket).setOnClickListener {
            if (currentInput == "0") currentInput = ""
            if (!currentInput.contains("(") || currentInput.endsWith(")")) {
                currentInput += "("
            } else {
                currentInput += ")"
            }
            tvDisplay.text = currentInput
        }

        // Clear Button (C)
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentInput = "0"
            tvDisplay.text = currentInput
        }

        // Backspace Button (⌫)
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (currentInput.isNotEmpty() && currentInput != "0") {
                currentInput = currentInput.dropLast(1)
                if (currentInput.isEmpty()) currentInput = "0"
                tvDisplay.text = currentInput
            }
        }

        // 4. Equals Button (=) -> Check Password FIRST, then Evaluate Math
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            val currentSavedPassword = sharedPrefs.getString("VAULT_PASSWORD", "")

            // Sabse pehle check karo ki user ne exact password dala hai ya nahi
            if (currentInput == currentSavedPassword && !currentSavedPassword.isNullOrEmpty()) {
                currentInput = "0"
                tvDisplay.text = currentInput
                val intent = Intent(this, VaultActivity::class.java)
                startActivity(intent)
            } else {
                // Agar password match nahi hua, tabhi normal math calculate karo
                try {
                    val result = calculateBasicMath(currentInput)
                    tvDisplay.text = result
                    currentInput = result
                } catch (e: Exception) {
                    tvDisplay.text = "Error"
                    currentInput = "0"
                }
            }
        }
    }

    // Simple & Safe Math Evaluator Function
    private fun calculateMath(expr: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if (ch in '0'.code..'9'.code || ch == '.'.code) {
                    while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                    x = expr.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                return x
            }
        }.parse()
    }

    private fun calculateBasicMath(str: String): String {
        val sanitized = str.replace("×", "*").replace("÷", "/")
        val res = calculateMath(sanitized)
        return if (res % 1.0 == 0.0) {
            res.toInt().toString()
        } else {
            res.toString()
        }
    }

    // First time setup with English Warning
    private fun showFirstTimeSetupDialog() {
        val input = EditText(this)
        input.hint = "Enter password (symbols, letters, nos)"
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("Setup Secret Password")
            .setMessage("WARNING: Please remember your password. You will need your old password if you want to change it later in the settings.")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val newPassword = input.text.toString()
                if (newPassword.isNotEmpty()) {
                    sharedPrefs.edit().putString("VAULT_PASSWORD", newPassword).apply()
                    Toast.makeText(this, "Password Saved Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                    showFirstTimeSetupDialog()
                }
            }
            .show()
    }

    // Password Reset Dialog (Triggered on 5 taps)
    private fun showResetPasswordDialog() {
        val inputOld = EditText(this)
        inputOld.hint = "Enter OLD Password"
        inputOld.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("Reset Password")
            .setMessage("Please enter your current password to proceed.")
            .setView(inputOld)
            .setPositiveButton("Next") { _, _ ->
                val oldPassInput = inputOld.text.toString()
                val actualOldPass = sharedPrefs.getString("VAULT_PASSWORD", "")

                if (oldPassInput == actualOldPass) {
                    showNewPasswordDialog()
                } else {
                    Toast.makeText(this, "Wrong Old Password!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Enter New Password Dialog
    private fun showNewPasswordDialog() {
        val inputNew = EditText(this)
        inputNew.hint = "Enter NEW Password"
        inputNew.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("New Password")
            .setView(inputNew)
            .setPositiveButton("Update") { _, _ ->
                val newPass = inputNew.text.toString()
                if (newPass.isNotEmpty()) {
                    sharedPrefs.edit().putString("VAULT_PASSWORD", newPass).apply()
                    Toast.makeText(this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }
}