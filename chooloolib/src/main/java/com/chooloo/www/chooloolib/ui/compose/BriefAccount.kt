package com.chooloo.www.chooloolib.ui.compose

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DriveEta
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil.compose.rememberAsyncImagePainter
import com.chooloo.www.chooloolib.R

data class Action(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val contentDescription: String? = null
)

@Composable
fun BriefAccount(
    title: String? = null,
    imageUri: Uri? = null,
    subtitle: String? = null,
    content: Composable? = null,
    actions: List<Action>? = null
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val spacing = dimensionResource(id = R.dimen.spacing)
        val spacingBig = dimensionResource(id = R.dimen.spacing_big)
        val spacingTiny = dimensionResource(id = R.dimen.spacing_tiny)
        val (titleRef, subtitleRef, imageRef, actionsRef) = createRefs()

        Text(
            maxLines = 2,
            text = title ?: "",
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = spacing)
                bottom.linkTo(subtitleRef.top)
                start.linkTo(parent.start, margin = spacingBig)
                end.linkTo(imageRef.start, margin = spacing)
            }
        )

        Text(
            text = subtitle ?: "",
            modifier = Modifier.constrainAs(subtitleRef) {
                start.linkTo(titleRef.start)
                top.linkTo(titleRef.bottom, margin = spacingTiny)
            }
        )

        Image(
            contentDescription = null,
            painter = rememberAsyncImagePainter(imageUri),
            modifier = Modifier
                .aspectRatio(1f)
                .constrainAs(subtitleRef) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    visibility =
                        if (title?.isNotEmpty() == true) Visibility.Visible else Visibility.Invisible
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        actions?.let {
            Row(
                modifier = Modifier.constrainAs(actionsRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(subtitleRef.bottom, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                    start.linkTo(parent.start, margin = spacing)
                }
            ) {
                it.forEach {
                    IconButton(onClick = { it.onClick.invoke() }) {
                        Icon(imageVector = it.icon, contentDescription = it.contentDescription)
                    }
                }
            }
        }

        content
    }
}

@Preview
@Composable
fun BriefAccountPreview() {
    BriefAccount(
        title = "title",
        subtitle = "subtitle",
        actions = listOf(Action(Icons.Rounded.DriveEta, {}, "Car"))
    )
}