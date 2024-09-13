package com.ct07.ttn.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.ct07.ttn.R
import com.ct07.ttn.models.Room
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress

public class AccountFragmentDirections private constructor() {
  private data class ActionAccountFragment2ToRoomFragment(
    public val room: Room
  ) : NavDirections {
    public override val actionId: Int = R.id.action_accountFragment2_to_roomFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  public companion object {
    public fun actionAccountFragment2ToRoomFragment(room: Room): NavDirections =
        ActionAccountFragment2ToRoomFragment(room)

    public fun actionAccountFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_accountFragment_to_loginFragment)
  }
}
