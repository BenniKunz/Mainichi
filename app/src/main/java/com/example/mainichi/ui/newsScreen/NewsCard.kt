package com.example.mainichi.ui.newsScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.api.news.Article
import com.example.mainichi.api.news.Source
import com.example.mainichi.ui.theme.MainichiTheme

@Composable
fun NewsCard(
    article: Article,
    expanded: Boolean,
    onCardClicked: () -> Unit
) {

    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .clickable { onCardClicked() }
    ) {

        Column(
            modifier = Modifier
                .padding(15.dp)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {

                if (!expanded) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp))
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    ) {

                        ImageLoader(data = article.urlToImage.toString())
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp))
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    ) {

                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = article.source.name,
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.h6
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = article.title,
                                    color = MaterialTheme.colors.secondaryVariant,
                                    style = MaterialTheme.typography.body1,
                                    maxLines = 7,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }


                    }
                } else {

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(shape = RoundedCornerShape(25.dp))
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(color = MaterialTheme.colors.onBackground)
                    ) {

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                if (article.content.isNullOrEmpty()) {
                                    Text("No content")
                                } else {
                                    Text(
                                        text = article.content,
                                        color = MaterialTheme.colors.secondaryVariant,
                                        maxLines = 10,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {

                                Text(
                                    text = "Full article: ${article.url.toString()}",
                                    color = MaterialTheme.colors.secondaryVariant
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.padding(end = 20.dp),
                    text = article.publishedAt,
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    textAlign = TextAlign.End,
                    text = when (article.author) {
                        null -> article.source.name

                        else -> {
                            article.author
                        }
                    },
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NewsCardPreview() {
    MainichiTheme() {
        Column() {
            NewsCard(
                Article(
                    author = "Helga",
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    description = "Nothing",
                    publishedAt = "2022-06-13T05:44:47Z",
                    source = Source(
                        id = "ID",
                        name = "NCAA.com"
                    ),
                    title = "2022 NCAA baseball bracket: Men's College World Series scores, schedule - NCAA.com test  test test test test test test test test test test test test test test test test test test test test test",
                    url = "https://www.formula1.com/en/latest/article.must-see-ferrari-suffer-double-dnf-in-baku-as-leclerc-retires-from-the-lead.40h2Bfr4OVbTEFu49beH4c.html",
                    urlToImage = "https://www.formula1.com/en/latest/article.must-see-ferrari-suffer-double-dnf-in-baku-as-leclerc-retires-from-the-lead.40h2Bfr4OVbTEFu49beH4c.html"
                ),
                expanded = false,
                {})
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NewsCardPreviewExpanded() {
    MainichiTheme() {
        Column() {
            NewsCard(
                Article(
                    author = "Helga",
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    description = "Nothing",
                    publishedAt = "2022-06-13T05:44:47Z",
                    source = Source(
                        id = "ID",
                        name = "NCAA.com"
                    ),
                    title = "2022 NCAA baseball bracket: Men's College World Series scores, schedule - NCAA.com test  test test test test test test test test test test test test test test test test test test test test test",
                    url = "https://www.formula1.com/en/latest/article.must-see-ferrari-suffer-double-dnf-in-baku-as-leclerc-retires-from-the-lead.40h2Bfr4OVbTEFu49beH4c.html",
                    urlToImage = "https://www.formula1.com/en/latest/article.must-see-ferrari-suffer-double-dnf-in-baku-as-leclerc-retires-from-the-lead.40h2Bfr4OVbTEFu49beH4c.html"
                ),
                expanded = true,
                {})
        }
    }
}
