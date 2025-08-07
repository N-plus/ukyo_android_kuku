@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kukutrainer.ui.terms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onAccept: () -> Unit = {},
    onBack: () -> Unit = {},
    showBackButton: Boolean = true
) {
    var hasScrolledToBottom by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // スクロール位置を監視
    LaunchedEffect(scrollState.value) {
        val maxScroll = scrollState.maxValue
        if (maxScroll > 0 && scrollState.value >= maxScroll - 100) {
            hasScrolledToBottom = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // ヘッダー
        TopAppBar(
            title = {
                Text(
                    "利用規約",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF81C784),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        // スクロール可能な利用規約内容
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            TermsContent()
        }

        // 同意ボタンエリア
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!hasScrolledToBottom) {
                    Text(
                        "利用規約を最後まで読んでから同意してください",
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = onAccept,
                    enabled = hasScrolledToBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = Color(0xFFBDBDBD)
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "利用規約に同意する",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TermsContent() {
    Column {
        // タイトル
        Text(
            text = "九九学習アプリ 利用規約",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "最終更新日：2025年8月4日",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // 前文
        SectionCard {
            Text(
                text = "本利用規約（以下「本規約」）は、未就学児向け九九学習アプリ（以下「本アプリ」）の利用条件を定めるものです。未就学児が本アプリを利用する場合は、保護者が本規約内容を確認・同意したものとみなします。",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF333333)
            )
        }

        // 各セクション
        TermsSection(
            title = "1. 適用",
            content = "本規約は、本アプリの利用に関する一切の関係に適用されます。未就学児が本アプリを利用する場合は、保護者の同意をもって本規約に同意したものとみなします。"
        )

        TermsSection(
            title = "2. 利用条件",
            content = """
                • 本アプリは、未就学児向けの学習支援を目的としています。
                • 保護者は、未就学児が本アプリを使用する際に適切に見守り、必要に応じて操作を補助してください。
                • 本アプリの利用にあたり、法令または本規約に違反する行為は禁止します。
            """.trimIndent()
        )

        TermsSection(
            title = "3. 保護者の同意と責任",
            content = "未就学児が本アプリを利用する場合、保護者が本規約内容を確認・同意し、利用状況を管理・監督する責任があります。保護者は、お子さまが本アプリを使用することに関して自己の判断で適切な管理を行ってください。"
        )

        TermsSection(
            title = "4. 収集する情報とその利用",
            content = """
                本アプリでは、以下の情報を最小限に収集する場合があります（収集しない設定も推奨）。
                • お子さまの学習進捗（どの段階まで覚えたか等）：端末内保存が基本
                • 設定情報（音のオン/オフ、表示モード等）
                
                ※氏名・住所・メールアドレス等の個人を特定する情報は未就学児から直接的に収集しません。
                ※収集した情報は、本アプリの機能向上と学習体験の提供のために使用します。第三者への提供は原則行いません。
                ※詳細はプライバシーポリシーを参照してください。
            """.trimIndent()
        )

        TermsSection(
            title = "5. 利用環境と端末上のデータ",
            content = "本アプリ内の学習進捗や設定は原則として端末内に保存されます。端末の紛失・故障等によるデータの消失について、開発者は一切の責任を負いません。"
        )

        TermsSection(
            title = "6. 禁止事項",
            content = """
                保護者および利用者は、以下の行為を行ってはなりません。
                • 本アプリを不正な目的で使用すること
                • 本規約または法令に違反する行為
                • 本アプリのソースコード・画像等を無断で転載・再配布すること（著作権侵害）
                • 本アプリの正常な運営を妨げる行為
            """.trimIndent()
        )

        TermsSection(
            title = "7. 知的財産権",
            content = "本アプリに関する一切のコンテンツ（デザイン、文言、音声、画像、プログラム等）の著作権その他の知的財産権は、開発者または正当な権利を有する第三者に帰属します。許可なく複製、改変、再配布等を行ってはいけません。"
        )

        TermsSection(
            title = "8. 免責事項",
            content = """
                • 本アプリは現状有姿で提供されます。開発者は、本アプリの完全性、正確性、特定の目的への適合性等について一切の保証を行いません。
                • 本アプリの利用に起因して発生した損害（データ消失、学習結果への影響等）について、開発者は責任を負いません（開発者に故意または重過失がある場合を除く）。
                • ネットワークや端末の問題による機能制限等についても責任を負いません。
            """.trimIndent()
        )

        TermsSection(
            title = "9. 本規約の変更",
            content = "開発者は必要に応じて本規約を予告なく変更することがあります。変更後の規約は本アプリ内に表示された時点から効力を持ち、引き続き本アプリを利用した場合、変更後の規約に同意したものとみなされます。"
        )

        TermsSection(
            title = "10. 準拠法および裁判管轄",
            content = "本規約の解釈および適用は日本法に準拠します。本アプリに関連する紛争が生じた場合、保護者の所在地を基準にした第一審の管轄裁判所を専属的合意管轄とします。"
        )

        // 最後のスペース
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TermsSection(title: String, content: String) {
    SectionCard {
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}
