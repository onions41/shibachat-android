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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun SentFReqCard(
  vm: FRequestsViewModel,
  sentFRequest: MeQuery.SentFRequest
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
      text = sentFRequest.sentFRequestFragment.friend.nickname,
      style = MaterialTheme.typography.body2,
      modifier = Modifier.width(140.dp).padding(horizontal = 12.dp)
    )

    Button(
      onClick = { vm.handle(FRequestsUiEvent.CancelFReqBtnClick(sentFRequest.friendId)) },
      enabled = vm.cancelFRequestResult is CancelFRequestResult.Ready,
      shape = CircleShape
    ) {
      Text(
        text = "Cancel request",
        modifier = Modifier.width(100.dp),
        style = MaterialTheme.typography.button,
        textAlign = TextAlign.Center
      )
    }
  }
}