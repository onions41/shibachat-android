package one.beefsupreme.shibachatandroid.ui.imagecomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.beefsupreme.shibachatandroid.R
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun FlowerDogeLogo() {
  // Sizes the outer white circle
  Box(
    modifier = Modifier
      .size(160.dp)
  ) {
    // Outer white circle
    Surface(
      color = MaterialTheme.colors.surface,
      shape = CircleShape,
      elevation = 8.dp,
      modifier = Modifier.align(Alignment.Center)
    ) {
      // Sizes the orange circle
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(142.dp)
      ) {
        // Background image with cherry blossoms
        Image(
          painter = painterResource(R.drawable.cherry_blossom_background_200x150),
          contentDescription = "cherry blossoms",
          contentScale = ContentScale.FillBounds,
          modifier = Modifier
            .size(135.dp)
            // Orange circle
            .border(
              width = 10.dp,
              color = MaterialTheme.colors.primaryVariant,
              shape = CircleShape
            )
            .clip(CircleShape)
        )
      }
    }
    // The doge with the flower on its head
    Image(
      painter = painterResource(R.drawable.flower_shiba_cutout_150x150),
      contentDescription = "Doge with flower on head",
      modifier = Modifier
        .size(149.dp)
        // Aligns in the x-axis then on the y-axis
        .align(BiasAbsoluteAlignment((-0.2).toFloat(), (-0.45).toFloat())),
    )
  }
}

@Preview
@Composable
fun DefaultPreview() {
  ShibachatAndroidTheme {
    FlowerDogeLogo()
  }
}