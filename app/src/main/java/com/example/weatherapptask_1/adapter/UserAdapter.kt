package com.example.weatherapptask_1.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask_1.databinding.UserlistLayoutBinding
import com.example.weatherapptask_1.model.User
import com.example.weatherapptask_1.ui.WeatherActivity

class UserAdapter(
    private val context: Context,
    private var users : List<User>) : RecyclerView.Adapter<UserAdapter.viewHolder>() {

    inner class viewHolder(val binding : UserlistLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapter.viewHolder {
        val binding = UserlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.viewHolder, position: Int) {
        val user = users[position]

        holder.binding.apply {
            firstNameTv.text = user.firstName
            lastNameTv.text = user.lastName
            emailTv.text = user.email

            root.setOnClickListener {
                val intent = Intent(context, WeatherActivity::class.java)
                intent.putExtra("city", "Varanasi") // You can pass dynamic city too
                context.startActivity(intent)
            }
        }
    }

    fun updateList(newList: List<User>) {
        users = newList
        notifyDataSetChanged()
    }

    fun getUserAt(position: Int): User {
        return users[position]
    }


    override fun getItemCount(): Int = users.size
}