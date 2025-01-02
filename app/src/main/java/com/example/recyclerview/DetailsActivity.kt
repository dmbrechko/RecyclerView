package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recyclerview.databinding.ActivityDetailsBinding
import com.example.recyclerview.databinding.DialogUpdateBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var cloth: Cloth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            cloth = intent.getParcelableCompat<Cloth>(KEY_CLOTH) ?: run {
                makeToast(R.string.error_loading_data)
                return@apply
            }
            val position = intent.getIntExtra(KEY_POSITION, 0)
            avatarIV.setImageResource(cloth.image)
            nameTV.text = String.format(getString(R.string.name), cloth.name)
            descTV.text = String.format(getString(R.string.description), cloth.desc)
            root.setOnLongClickListener {
                val builder = AlertDialog.Builder(this@DetailsActivity)
                val dialogBinding = DialogUpdateBinding.inflate(layoutInflater)
                dialogBinding.apply {
                    nameET.setText(cloth.name)
                    descET.setText(cloth.desc)
                }
                builder
                    .setView(dialogBinding.root)
                    .setTitle(getString(R.string.edit_cloth_info))
                    .setPositiveButton(getString(R.string.update)) { _, _ ->
                        dialogBinding.apply {
                            if (nameET.text.isBlank() || descET.text.isBlank()) {
                                makeToast(R.string.fill_all_fields)
                                return@setPositiveButton
                            }
                            cloth = cloth.copy(
                                name = nameET.text.toString(),
                                desc = descET.text.toString()
                            )
                            nameTV.text = String.format(getString(R.string.name), cloth.name)
                            descTV.text = String.format(getString(R.string.description), cloth.desc)
                            val intent = Intent().apply {
                                putExtra(KEY_CLOTH, cloth)
                                putExtra(KEY_POSITION, position)
                            }
                            setResult(RESULT_OK, intent)
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _, -> }
                    .create().show()
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_exit -> {
                moveTaskToBack(true)
                finish()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val KEY_CLOTH = "key cloth"
        const val KEY_POSITION = "key position"
    }
}