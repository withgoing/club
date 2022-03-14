package org.withgoing.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.withgoing.club.entity.Note;
import org.withgoing.club.repository.NoteRepository;
import org.withgoing.club.security.dto.NoteDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteDTO noteDTO) {

        Note note = dtoToEntity(noteDTO);

        log.info("=============================================");
        log.info(note);

        noteRepository.save(note);

        return note.getNum();
    }

    @Override
    public NoteDTO get(Long num) {

        Optional<Note> result = noteRepository.getWithWriter(num);

        if(result.isPresent()) {
            return entityToDTO(result.get());
        }

        return null;
    }

    @Override
    public void modify(NoteDTO noteDTO) {

        Optional<Note> result = noteRepository.getWithWriter(noteDTO.getNum());

        if(result.isPresent()) {
            Note note = result.get();
            note.changeTitle(noteDTO.getTitle());
            note.changeContent(note.getContent());

            noteRepository.save(note);
        }
    }

    @Override
    public void remove(Long num) {

        noteRepository.deleteById(num);
    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {

        List<Note> noteList = noteRepository.getList(writerEmail);

        return noteList.stream().map(note -> entityToDTO(note)).collect(Collectors.toList());
    }
}
