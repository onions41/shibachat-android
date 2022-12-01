package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.beefsupreme.shibachatandroid.R
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun MakeNewFriendsLinkCard(handleClick: () -> Unit) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .height(65.dp)
      .clickable(onClickLabel = "make-new-friends") { handleClick() }
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
      text = "Make new friends!",
      style = MaterialTheme.typography.h6,
      modifier = Modifier.padding(start = 12.dp)
    )
  }
}

@Preview(widthDp = 360)
@Composable
fun MakeNewFriendsCardPreview() {
  ShibachatAndroidTheme {
    Surface {
      MakeNewFriendsLinkCard {}
    }
  }
}
