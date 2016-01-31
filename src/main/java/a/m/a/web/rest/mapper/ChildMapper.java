package a.m.a.web.rest.mapper;

import a.m.a.domain.*;
import a.m.a.web.rest.dto.ChildDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Child and its DTO ChildDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChildMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    ChildDTO childToChildDTO(Child child);

    @Mapping(source = "parentId", target = "parent")
    Child childDTOToChild(ChildDTO childDTO);

    default Parent parentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Parent parent = new Parent();
        parent.setId(id);
        return parent;
    }
}
