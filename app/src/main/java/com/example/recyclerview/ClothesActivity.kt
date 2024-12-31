package com.example.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityClothesBinding

class ClothesActivity : AppCompatActivity() {
    private val clothes: List<Cloth> = List(20) { index ->
        Cloth(
            image = R.drawable.cloth,
            name = "Cloth Item #${index + 1}",
            desc = "Description for Cloth Item #${index + 1}"
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityClothesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            setSupportActionBar(toolbar)
            listRV.layoutManager = LinearLayoutManager(this@ClothesActivity)
            listRV.adapter = ClothAdapter(clothes)
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
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

data class Cloth(val image: Int, val name: String, val desc: String)