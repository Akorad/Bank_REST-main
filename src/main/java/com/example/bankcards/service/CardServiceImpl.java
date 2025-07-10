package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.dto.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidCardStatusException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserService userService;
    private final EncryptionUtils encryptionUtils;



    @Override
    public CardResponseDTO createCard() {
        User user = userService.getCurrentUser();

        String fullName = transliterate(user.getLastName() + " " + user.getFirstName());

        Card card = new Card();
        card.setNumber(generateUniqueCardNumber(user.getUsername()));
        card.setExpirationDate(generateExpirationDate());
        card.setOwnerName(fullName);
        card.setUser(user);
        card.setStatus(CardStatus.ACTIVE);

        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    public CardResponseDTO updateStatus(Long cardId, String status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        try {
            card.setStatus(CardStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidCardStatusException(status);
        }
        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        cardRepository.delete(card);
    }

    @Override
    public CardResponseDTO getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        return cardMapper.toDto(card);
    }

    @Override
    public Page<CardResponseDTO> getAllCardsForAdmin(Pageable pageable) {
        return cardRepository.findAll(pageable).map(cardMapper::toDto);
    }

    @Override
    public Page<CardResponseDTO> getMyCards(Pageable pageable) {
        return getMyCards(null, pageable);
    }

    @Override
    public Page<CardResponseDTO> getMyCards(CardStatus status, Pageable pageable) {
        User user = userService.getCurrentUser();

        Page<Card> cards = (status != null)
                ? cardRepository.findAllByUserAndStatus(user, status, pageable)
                : cardRepository.findAllByUser(user, pageable);

        return cards.map(cardMapper::toDto);
    }

    @Override
    public Page<CardResponseDTO> getAllCardsForAdmin(CardStatus status, Pageable pageable) {
        Page<Card> cards = (status != null)
                ? cardRepository.findAllByStatus(status, pageable)
                : cardRepository.findAll(pageable);

        return cards.map(cardMapper::toDto);
    }

    private String generateCardNumber(String username) {
        String prefix = "4571"; // фиксированный префикс банка

        // Используем хэш от логина, чтобы всегда получать одно и то же для одного юзера
        int hash = Math.abs(username.hashCode());
        String userPart = String.format("%04d", hash % 10000);

        String randomPart = String.format("%04d", new Random().nextInt(10000));
        String yearPart = String.valueOf(LocalDate.now().getYear());

        return prefix + userPart + randomPart + yearPart;
    }

    private String generateUniqueCardNumber(String username) {
        String candidate;
        int attempts = 0;
        do {
            if (++attempts > 10) throw new RuntimeException("Не удалось сгенерировать уникальный номер карты");

            candidate = generateCardNumber(username);
        } while (cardRepository.existsByNumber(encryptionUtils.encrypt(candidate)));

        return candidate;
    }

    private String transliterate(String cyrillicName) {
        Map<Character, String> map = new HashMap<>();
        
        map.put('А', "A");  map.put('а', "a"); map.put('Б', "B");  map.put('б', "b");
        map.put('В', "V");  map.put('в', "v"); map.put('Г', "G");  map.put('г', "g");
        map.put('Д', "D");  map.put('д', "d"); map.put('Е', "E");  map.put('е', "e");
        map.put('Ё', "E");  map.put('ё', "e"); map.put('Ж', "Zh"); map.put('ж', "zh");
        map.put('З', "Z");  map.put('з', "z"); map.put('И', "I");  map.put('и', "i");
        map.put('Й', "Y");  map.put('й', "y"); map.put('К', "K");  map.put('к', "k");
        map.put('Л', "L");  map.put('л', "l"); map.put('М', "M");  map.put('м', "m");
        map.put('Н', "N");  map.put('н', "n"); map.put('О', "O");  map.put('о', "o");
        map.put('П', "P");  map.put('п', "p"); map.put('Р', "R");  map.put('р', "r");
        map.put('С', "S");  map.put('с', "s"); map.put('Т', "T");  map.put('т', "t");
        map.put('У', "U");  map.put('у', "u"); map.put('Ф', "F");  map.put('ф', "f");
        map.put('Х', "Kh"); map.put('х', "kh"); map.put('Ц', "Ts"); map.put('ц', "ts");
        map.put('Ч', "Ch"); map.put('ч', "ch"); map.put('Ш', "Sh"); map.put('ш', "sh");
        map.put('Щ', "Shch"); map.put('щ', "shch"); map.put('Ы', "Y");  map.put('ы', "y");
        map.put('Э', "E");  map.put('э', "e"); map.put('Ю', "Yu"); map.put('ю', "yu");
        map.put('Я', "Ya"); map.put('я', "ya"); map.put('Ь', "");   map.put('ь', "");
        map.put('Ъ', "");   map.put('ъ', "");

        return cyrillicName.chars()
                .mapToObj(c -> {
                    char ch = (char) c;
                    String upper = map.getOrDefault(Character.toUpperCase(ch), String.valueOf(ch));
                    return Character.isUpperCase(ch) ? upper : upper.toLowerCase();
                })
                .collect(Collectors.joining());
    }
    private String generateExpirationDate() {
        LocalDate expiration = LocalDate.now().plusYears(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return expiration.format(formatter);
    }

}
