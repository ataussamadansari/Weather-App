package com.example.weatherapptask_1

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask_1.adapter.UserAdapter
import com.example.weatherapptask_1.databinding.ActivityMainBinding
import com.example.weatherapptask_1.ui.AddUserActivity
import com.example.weatherapptask_1.ui.AuthActivity
import com.example.weatherapptask_1.utils.SharedPrefHelper
import com.example.weatherapptask_1.viewmodel.UserListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var addUserFab: ExtendedFloatingActionButton
    private lateinit var userRv: RecyclerView
    private lateinit var userListViewModel: UserListViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(16, systemBars.top, 16, systemBars.bottom)
            insets
        }

        // ViewModel setup
        userListViewModel = ViewModelProvider(this)[UserListViewModel::class.java]

        // Adapter setup
        userAdapter = UserAdapter(this, emptyList())
        binding.userRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        // binding setup
        addUserFab = binding.addUserFab
        userRv = binding.userRv

        // LiveData observer
        userListViewModel.allNotes.observe(this) { notes ->
            userAdapter.updateList(notes)
        }

        // Logout
        binding.logoutBtn.setOnClickListener {
            /* SharedPrefHelper.logout(this)

             Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
             startActivity(Intent(this, AuthActivity::class.java))
             finish()*/

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setCancelable(false)
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            builder.setPositiveButton("Yes") { dialog, _ ->
                SharedPrefHelper.logout(this)
                dialog.dismiss()
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        // Add new user
        addUserFab.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }

        // Shrink and Extend setup
        userRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10 && addUserFab.isExtended) {
                    addUserFab.shrink()
                } else if (dy < -10 && !addUserFab.isExtended) {
                    addUserFab.extend()
                }
            }
        })


        // swipe delete setup
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                val user = userAdapter.getUserAt(position)

                /* userListViewModel.delete(deletedUser)
                 Toast.makeText(this@MainActivity, "User Deleted", Toast.LENGTH_SHORT).show()*/

                if (direction == ItemTouchHelper.LEFT) {
                    // 🔴 Delete
                    userListViewModel.delete(user)
                    Toast.makeText(
                        this@MainActivity,
                        "Deleted: ${user.firstName}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // 🟢 Edit: Launch AddUserActivity in Edit mode
                    val intent = Intent(this@MainActivity, AddUserActivity::class.java)
                    intent.putExtra("userId", user.id) // pass ID or full user object
                    startActivity(intent)
                    userAdapter.notifyItemChanged(position) // to reset swipe state
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Drawable
                val background: ColorDrawable

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                if (dX > 0) { // Swiping right
                    icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_user_edit)!!
                    background = ColorDrawable(Color.parseColor("#388E3C"))
                } else { // Swiping left
                    icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_trash)!!
                    background = ColorDrawable(Color.parseColor("#D32F2F"))
                }

                // Draw background
                background.setBounds(
                    if (dX > 0) itemView.left else itemView.right + dX.toInt(),
                    itemView.top,
                    if (dX > 0) itemView.left + dX.toInt() + backgroundCornerOffset else itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                // Calculate icon position
                val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight

                if (dX > 0) {
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = iconLeft + icon.intrinsicWidth
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else {
                    val iconRight = itemView.right - iconMargin
                    val iconLeft = iconRight - icon.intrinsicWidth
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                }

                icon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.userRv)
    }
}