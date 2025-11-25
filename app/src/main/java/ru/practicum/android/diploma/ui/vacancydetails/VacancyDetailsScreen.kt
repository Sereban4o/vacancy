package ru.practicum.android.diploma.ui.vacancydetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import ru.practicum.android.diploma.ui.components.formatSalary
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyContacts
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.presentation.vacancy_details_screen.VacancyDetailsUiState
import ru.practicum.android.diploma.presentation.vacancy_details_screen.VacancyDetailsViewModel

@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: VacancyDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (uiState) {
        is VacancyDetailsUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is VacancyDetailsUiState.Error -> {
            VacancyDetailsErrorPlaceholder(
                isNetworkError = (uiState as VacancyDetailsUiState.Error).isNetworkError,
                onRetryClick = { viewModel.loadDetails() }
            )
        }

        is VacancyDetailsUiState.Content -> {
            val vacancy = (uiState as VacancyDetailsUiState.Content).vacancy
            VacancyDetailsContent(
                vacancy = vacancy,
                onBack = onBack,
                onShareClick = { shareVacancy(context, vacancy.vacancyUrl) },
                onEmailClick = { email -> openEmail(context, email) },
                onPhoneClick = { phone -> openPhone(context, phone) }
            )
        }
    }
}

@Composable
fun VacancyDetailsContent(
    vacancy: VacancyDetails,
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painterResource(R.drawable.ic_arrow_back_24),
                    contentDescription = "Назад"
                )
            }
            Row {
                IconButton(onClick = onShareClick) {
                    Icon(painterResource(R.drawable.ic_share_24), contentDescription = "Поделиться")
                }
                IconButton(onClick = { /* TODO избранное */ }) {
                    Icon(painterResource(R.drawable.ic_favorites_24), contentDescription = "Избранное")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = vacancy.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(Modifier.height(8.dp))

        // 💰 Зарплата
        Text(
            text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.currency),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        // 🏢 Компания
        CompanyCard(vacancy)

        Spacer(Modifier.height(24.dp))

        // 📌 Требуемый опыт
        Text("Требуемый опыт", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        vacancy.experience?.let { Text(it) }
        Spacer(Modifier.height(8.dp))
        Text("${vacancy.employment}, ${vacancy.schedule}")

        Spacer(Modifier.height(24.dp))

        // 📝 Описание
        Text("Описание вакансии", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        DescriptionBlock(vacancy.description)

        Spacer(Modifier.height(24.dp))

        // ⭐ Навыки
        if (vacancy.skills.isNotEmpty()) {
            Text("Ключевые навыки", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            vacancy.skills.forEach {
                Text("• $it")
                Spacer(Modifier.height(4.dp))
            }
            Spacer(Modifier.height(24.dp))
        }

        // 📞 Контакты
        vacancy.contacts?.let { contacts ->
            if (contacts.email != null || contacts.phones.isNotEmpty()) {
                Text("Контакты", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                ContactsBlock(
                    contacts,
                    onEmailClick = onEmailClick,
                    onPhoneClick = onPhoneClick
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun CompanyCard(vacancy: VacancyDetails) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F3F7), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(vacancy.logoUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_company_placeholder),
            error = painterResource(R.drawable.ic_company_placeholder),
            contentDescription = vacancy.companyName,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(vacancy.companyName, fontWeight = FontWeight.SemiBold)
            (vacancy.address ?: vacancy.region)?.let { Text(it, color = Color.Gray) }
        }
    }
}

@Composable
fun DescriptionBlock(text: String) {
    text.split("\n").forEach { line ->
        if (line.startsWith("-") || line.startsWith("•")) {
            Row {
                Text("• ")
                Text(line.removePrefix("-").trim())
            }
        } else {
            Text(line)
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun ContactsBlock(
    contacts: VacancyContacts,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    contacts.email?.let {
        Text(
            it,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onEmailClick(it) }
        )
        Spacer(Modifier.height(4.dp))
    }

    contacts.phones.forEach { phone ->
        Text(
            phone,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onPhoneClick(phone) }
        )
        Spacer(Modifier.height(4.dp))
    }

    contacts.comment?.let {
        Spacer(Modifier.height(8.dp))
        Text(it, color = Color.Gray)
    }
}

fun shareVacancy(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
    context.startActivity(Intent.createChooser(intent, "Поделиться вакансией"))
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
    context.startActivity(intent)
}

fun openPhone(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}


//package ru.practicum.android.diploma.ui.vacancydetails
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import ru.practicum.android.diploma.R
//import ru.practicum.android.diploma.domain.models.VacancyContacts
//import ru.practicum.android.diploma.domain.models.VacancyDetails
//import ru.practicum.android.diploma.presentation.vacancy_details_screen.VacancyDetailsUiState
//import ru.practicum.android.diploma.presentation.vacancy_details_screen.VacancyDetailsViewModel
//import ru.practicum.android.diploma.ui.components.formatSalary
//
//@Composable
//fun VacancyDetailsScreen(
//    vacancyId: String,
//    modifier: Modifier = Modifier,
//    onBack: () -> Unit,
//    viewModel: VacancyDetailsViewModel // получаешь через DI снаружи (Koin/Hilt), см. ниже
//) {
//    val state by viewModel.uiState.collectAsState()
//    val context = LocalContext.current
//
//    when (state) {
//        is VacancyDetailsUiState.Loading -> {
//            Box(
//                modifier = modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//
//        is VacancyDetailsUiState.Error -> {
//            val error = state as VacancyDetailsUiState.Error
//            VacancyDetailsErrorPlaceholder(
//                modifier = modifier.fillMaxSize(),
//                isNetworkError = error.isNetworkError,
//                onRetryClick = { viewModel.loadDetails() }
//            )
//        }
//
//        is VacancyDetailsUiState.Content -> {
//            val vacancy = (state as VacancyDetailsUiState.Content).vacancy
//            VacancyDetailsContent(
//                modifier = modifier.fillMaxSize(),
//                vacancy = vacancy,
//                onBack = onBack,
//                onShareClick = { shareVacancy(context, vacancy.vacancyUrl) },
//                onEmailClick = { email -> openEmail(context, email) },
//                onPhoneClick = { phone -> openPhone(context, phone) }
//            )
//        }
//    }
//}
//
//@Composable
//fun VacancyDetailsErrorPlaceholder(
//    modifier: Modifier = Modifier,
//    isNetworkError: Boolean,
//    onRetryClick: () -> Unit
//) {
//    Box(
//        modifier = modifier.padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = if (isNetworkError) {
//                    "Нет подключения к интернету"
//                } else {
//                    "Произошла ошибка при загрузке вакансии"
//                },
//                style = MaterialTheme.typography.bodyLarge
//            )
//            Spacer(Modifier.height(8.dp))
//            Button(onClick = onRetryClick) {
//                Text("Повторить")
//            }
//        }
//    }
//}
//
//@Composable
//fun VacancyDetailsContent(
//    modifier: Modifier = Modifier,
//    vacancy: VacancyDetails,
//    onBack: () -> Unit,
//    onShareClick: () -> Unit,
//    onEmailClick: (String) -> Unit,
//    onPhoneClick: (String) -> Unit
//) {
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = modifier
//            .padding(vertical = 16.dp)
//            .verticalScroll(scrollState)
//    ) {
//        // Верхняя панель: кнопка назад + share
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_arrow_back_24), // добавь ресурс
//                    contentDescription = "Назад"
//                )
//            }
//
//            IconButton(onClick = onShareClick) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_share_24), // добавь ресурс
//                    contentDescription = "Поделиться"
//                )
//            }
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        // Название вакансии
//        Text(
//            text = vacancy.title,
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // Блок: компания + адрес/регион + логотип
//        CompanyBlock(
//            vacancy = vacancy,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // Зарплата — используем уже существующий форматтер из ui.components
//        Text(
//            text = formatSalary(
//                salaryFrom = vacancy.salaryFrom,
//                salaryTo = vacancy.salaryTo,
//                currencyCode = vacancy.currency
//            ),
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // Описание
//        Text(
//            text = vacancy.description,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // Ключевые навыки (если есть)
//        if (vacancy.skills.isNotEmpty()) {
//            SkillsBlock(
//                skills = vacancy.skills,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            )
//            Spacer(Modifier.height(16.dp))
//        }
//
//        // Контакты (если есть)
//        vacancy.contacts?.let { contacts ->
//            if (contacts.email != null || contacts.phones.isNotEmpty() || contacts.comment != null) {
//                ContactsBlock(
//                    contacts = contacts,
//                    onEmailClick = onEmailClick,
//                    onPhoneClick = onPhoneClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                )
//                Spacer(Modifier.height(16.dp))
//            }
//        }
//
//        Spacer(Modifier.height(24.dp))
//    }
//}
//
//@Composable
//fun CompanyBlock(
//    vacancy: VacancyDetails,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        val logoSize = 48.dp
//
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(vacancy.logoUrl)
//                .crossfade(true)
//                .build(),
//            contentDescription = vacancy.companyName,
//            placeholder = painterResource(R.drawable.ic_company_placeholder),
//            error = painterResource(R.drawable.ic_company_placeholder),
//            modifier = Modifier.size(logoSize)
//        )
//
//        Spacer(Modifier.width(12.dp))
//
//        Column {
//            Text(
//                text = vacancy.companyName,
//                style = MaterialTheme.typography.titleMedium,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            val addressOrRegion = vacancy.address ?: vacancy.region
//
//            addressOrRegion?.let {
//                Text(
//                    text = it,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SkillsBlock(
//    skills: List<String>,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier) {
//        Text(
//            text = "Ключевые навыки",
//            style = MaterialTheme.typography.titleMedium
//        )
//        Spacer(Modifier.height(8.dp))
//        skills.forEach { skill ->
//            Text(
//                text = "• $skill",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}
//
//@Composable
//fun ContactsBlock(
//    contacts: VacancyContacts,
//    onEmailClick: (String) -> Unit,
//    onPhoneClick: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier) {
//        Text(
//            text = "Контакты",
//            style = MaterialTheme.typography.titleMedium
//        )
//        Spacer(Modifier.height(8.dp))
//
//        contacts.email?.let { email ->
//            Text(
//                text = email,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.clickable { onEmailClick(email) }
//            )
//            Spacer(Modifier.height(4.dp))
//        }
//
//        contacts.phones.forEach { phone ->
//            Text(
//                text = phone,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.clickable { onPhoneClick(phone) }
//            )
//            Spacer(Modifier.height(4.dp))
//        }
//
//        contacts.comment?.let { comment ->
//            Spacer(Modifier.height(4.dp))
//            Text(
//                text = comment,
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//    }
//}
//
//// ---------- Intents: share, email, phone ----------
//
//fun shareVacancy(context: Context, url: String) {
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"
//        putExtra(Intent.EXTRA_TEXT, url)
//    }
//    context.startActivity(
//        Intent.createChooser(intent, "Поделиться вакансией")
//    )
//}
//
//@SuppressLint("UseKtx")
//fun openEmail(context: Context, email: String) {
//    val intent = Intent(Intent.ACTION_SENDTO).apply {
//        data = Uri.parse("mailto:$email")
//    }
//    context.startActivity(intent)
//}
//
//@SuppressLint("UseKtx")
//fun openPhone(context: Context, phone: String) {
//    val intent = Intent(Intent.ACTION_DIAL).apply {
//        data = Uri.parse("tel:$phone")
//    }
//    context.startActivity(intent)
//}
