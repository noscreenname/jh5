package a.m.a.web.rest;

import com.codahale.metrics.annotation.Timed;
import a.m.a.domain.Child;
import a.m.a.repository.ChildRepository;
import a.m.a.web.rest.util.HeaderUtil;
import a.m.a.web.rest.util.PaginationUtil;
import a.m.a.web.rest.dto.ChildDTO;
import a.m.a.web.rest.mapper.ChildMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);
        
    @Inject
    private ChildRepository childRepository;
    
    @Inject
    private ChildMapper childMapper;
    
    /**
     * POST  /childs -> Create a new child.
     */
    @RequestMapping(value = "/childs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChildDTO> createChild(@RequestBody ChildDTO childDTO) throws URISyntaxException {
        log.debug("REST request to save Child : {}", childDTO);
        if (childDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("child", "idexists", "A new child cannot already have an ID")).body(null);
        }
        Child child = childMapper.childDTOToChild(childDTO);
        child = childRepository.save(child);
        ChildDTO result = childMapper.childToChildDTO(child);
        return ResponseEntity.created(new URI("/api/childs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("child", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /childs -> Updates an existing child.
     */
    @RequestMapping(value = "/childs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChildDTO> updateChild(@RequestBody ChildDTO childDTO) throws URISyntaxException {
        log.debug("REST request to update Child : {}", childDTO);
        if (childDTO.getId() == null) {
            return createChild(childDTO);
        }
        Child child = childMapper.childDTOToChild(childDTO);
        child = childRepository.save(child);
        ChildDTO result = childMapper.childToChildDTO(child);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("child", childDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /childs -> get all the childs.
     */
    @RequestMapping(value = "/childs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ChildDTO>> getAllChilds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Childs");
        Page<Child> page = childRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/childs");
        return new ResponseEntity<>(page.getContent().stream()
            .map(childMapper::childToChildDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /childs/:id -> get the "id" child.
     */
    @RequestMapping(value = "/childs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChildDTO> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Child child = childRepository.findOne(id);
        ChildDTO childDTO = childMapper.childToChildDTO(child);
        return Optional.ofNullable(childDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /childs/:id -> delete the "id" child.
     */
    @RequestMapping(value = "/childs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("child", id.toString())).build();
    }
}
