package com.ct07.ttn.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.ct07.ttn.models.Room
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class RoomFragmentArgs(
  public val room: Room
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(Room::class.java)) {
      result.putParcelable("room", this.room as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Room::class.java)) {
      result.putSerializable("room", this.room as Serializable)
    } else {
      throw UnsupportedOperationException(Room::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(Room::class.java)) {
      result.set("room", this.room as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Room::class.java)) {
      result.set("room", this.room as Serializable)
    } else {
      throw UnsupportedOperationException(Room::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): RoomFragmentArgs {
      bundle.setClassLoader(RoomFragmentArgs::class.java.classLoader)
      val __room : Room?
      if (bundle.containsKey("room")) {
        if (Parcelable::class.java.isAssignableFrom(Room::class.java) ||
            Serializable::class.java.isAssignableFrom(Room::class.java)) {
          __room = bundle.get("room") as Room?
        } else {
          throw UnsupportedOperationException(Room::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__room == null) {
          throw IllegalArgumentException("Argument \"room\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"room\" is missing and does not have an android:defaultValue")
      }
      return RoomFragmentArgs(__room)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): RoomFragmentArgs {
      val __room : Room?
      if (savedStateHandle.contains("room")) {
        if (Parcelable::class.java.isAssignableFrom(Room::class.java) ||
            Serializable::class.java.isAssignableFrom(Room::class.java)) {
          __room = savedStateHandle.get<Room?>("room")
        } else {
          throw UnsupportedOperationException(Room::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__room == null) {
          throw IllegalArgumentException("Argument \"room\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"room\" is missing and does not have an android:defaultValue")
      }
      return RoomFragmentArgs(__room)
    }
  }
}
