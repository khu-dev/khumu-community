//package com.khumu.community.common.mapper;
//
//import com.khumu.community.application.dto.SimpleUserDto;
//import com.khumu.community.application.dto.UserDto;
//import com.khumu.community.application.dto.UserEventDto;
//import com.khumu.community.application.entity.Group;
//import com.khumu.community.application.entity.User;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//
//@Mapper(
//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.IGNORE,
//        uses= {}
//)
//public interface GroupMapper {
//    // TODO: 이거 status delete일 때도 생각해야하는데
//    // 영컨의 User가 아니라 간이로 만든 유저가 전달되는 경우도 있음.
//    @Mapping(target=".", source = "id")
//    Integer toInt(Group src);
//}