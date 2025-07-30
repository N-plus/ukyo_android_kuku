@Composable
fun KukuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            surface = KukuColor.Surface,
            primary = KukuColor.Accent,
            background = KukuColor.Bg
        ),
        typography = KukuTypography,
        content = content
    )
}