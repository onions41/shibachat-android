package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import one.beefsupreme.shibachatandroid.MeQuery
import one.beefsupreme.shibachatandroid.R

@Composable
fun ReceivedFReqCard(
  fRequest: MeQuery.ReceivedFRequest,
  vm: FriendsViewModel
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .height(65.dp)
      .padding(horizontal = 12.dp)
  ) {
    // Avatar
    Image(
      painter = painterResource(R.drawable.cherry_blossom_background_200x150),
      contentDescription = "cherry blossoms",
      contentScale = ContentScale.FillBounds,
      modifier = Modifier
        .size(50.dp)
        // Orange circle
        .clip(RoundedCornerShape(8.dp))
    )

    Text(
      text = "${fRequest.me.nickname} wants to be your friend. Do you accept?",
      style = MaterialTheme.typography.caption,
      modifier = Modifier.width(140.dp)
    )

    Button(
      onClick = { vm.handle(FriendsUiEvent.AcceptFReqBtnClick(fRequest.meId)) },
      shape = CircleShape,
    ) {
      Text(
        text = "Yes!",
        modifier = Modifier.width(40.dp),
        textAlign = TextAlign.Center
      )
    }

    TextButton(
      onClick = {},
      shape = MaterialTheme.shapes.small,
      colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)
    ) {
      Text(
        text = "DO NOT WANT!",
        style = MaterialTheme.typography.caption,
        modifier = Modifier.width(50.dp),
        textAlign = TextAlign.Center
      )
    }
  }
}

//@Preview(widthDp = 360)
//@Composable
//fun ReceivedFReqCardPreview() {
//  ShibachatAndroidTheme {
//    Surface {
//      ReceivedFReqCard(
//        MeQuery.ReceivedFRequest(
//          meId = 1,
//          friendId = 2,
//          me = MeQuery.Me(id = 1, nickname = "Homer", __typename = "User"),
//          status = FRequestStatus.SENT,
//          __typename = "FriendRequest"
//        ),
//        vm = hiltViewModel()
//      )
//    }
//  }
//}