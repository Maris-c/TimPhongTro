package com.ct07.ttn.adapters

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ct07.ttn.R
import com.ct07.ttn.models.Room
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.ui.user.UserViewModel
import java.text.NumberFormat
import java.util.*

class RoomAdapter(private val roomViewModel: RoomViewModel,
                  private val userViewModel: UserViewModel,
                  private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var roomImage: ImageView
    lateinit var roomTitle: TextView
    lateinit var roomAddress: TextView
    lateinit var roomPrice: TextView
    lateinit var roomFavourite: ImageView
    lateinit var roomArea: TextView

    var isLogin: Boolean = false
    var isFavorite: Boolean = false
    var user: User? = null

    private val differCallback = object : DiffUtil.ItemCallback<Room>(){
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Room) -> Unit)? = null

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        var room = differ.currentList[position]
        isLogin = userViewModel.isLogin()

        roomImage = holder.itemView.findViewById(R.id.roomImage)
        roomTitle = holder.itemView.findViewById(R.id.roomTitle)
        roomAddress = holder.itemView.findViewById(R.id.roomAddress)
        roomPrice = holder.itemView.findViewById(R.id.roomPrice)
        roomFavourite = holder.itemView.findViewById(R.id.roomFavorite)
        roomArea = holder.itemView.findViewById(R.id.roomArea)

        holder.itemView.apply {
            if (room.image.uri_image != null && room.image.uri_image.isNotEmpty()) {
                Glide.with(this).load(room.image.uri_image[0].toUri()).into(roomImage)
            }
            roomTitle.text = room.title
            roomAddress.text = (room.address.road + ", " +
                                room.address.ward + ", " +
                                room.address.district + ", " +
                                room.address.province)

            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
            roomPrice.text = numberFormat.format(room.price) + " VNĐ"
            roomArea.text = room.area + "m²"

            setOnClickListener {
                onItemClickListener?.let {
                    it(room)
                }
            }

            if(isLogin){
                user = userViewModel.getUser()

                roomViewModel.favorites.observe(lifecycleOwner, { favorites ->
                    favorites?.let {
                        if (it.data != null){
                            val favorite = it.data.find { it.id == room.id }
                            if (favorite != null){
                                isFavorite = true
                                roomFavourite.setImageResource(R.drawable.baseline_favorite_24)
                            } else {
                                isFavorite = false
                                roomFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                        }
                    }
                })

                roomFavourite.setOnClickListener {
                    if (!isFavorite) {
                        roomViewModel.addToFavorites(user?.id ?: 0, room.id)
                        Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                        roomFavourite.setImageResource(R.drawable.baseline_favorite_24)
                        roomViewModel.getHeadlines()
                    } else if (isFavorite) {
                        roomViewModel.unFavorites(user?.id ?: 0, room.id)
                        Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                        roomFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
                        roomViewModel.getHeadlines()
                    } else {
                        Toast.makeText(context, "Đã có lỗi đồng bộ xảy ra", Toast.LENGTH_SHORT).show()
                    }
                    isFavorite = !isFavorite
                }
            } else {
                roomFavourite.setOnClickListener {
                    Toast.makeText(context, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Room) -> Unit){
        onItemClickListener = listener
    }
}