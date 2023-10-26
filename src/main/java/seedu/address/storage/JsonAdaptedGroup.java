package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TimeIntervalList;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupRemark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Jackson-friendly version of {@link Group}.
 */
class JsonAdaptedGroup {

    private final String groupName;
    private final String groupRemark;
    private final List<JsonAdaptedTime> meetingTimeList = new ArrayList<>();
//    private final List<JsonAdaptedPerson> listOfGroupMates = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given {@code groupName}.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("name") String groupName, @JsonProperty("groupRemark") String groupRemark,
                            //@JsonProperty("listOfGroupMates") List<JsonAdaptedPerson> listOfGroupMates,
                            @JsonProperty("meetingTimeList") List<JsonAdaptedTime> meetingTimeList) {
        this.groupName = groupName;
        this.groupRemark = groupRemark;
//        if (listOfGroupMates != null) {
//            this.listOfGroupMates.addAll(listOfGroupMates);
//        }
        if (meetingTimeList != null) {
            this.meetingTimeList.addAll(meetingTimeList);
        }
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        groupName = source.getGroupName();
        groupRemark = source.getGroupRemark().value;
//        if (listOfGroupMates!= null) {
//            listOfGroupMates.addAll(source.toStream()
//                    .map(JsonAdaptedPerson::new)
//                    .collect(Collectors.toList()));
//        }


        if (meetingTimeList != null) {
            meetingTimeList.addAll(source.getTime().toStream()
                    .map(JsonAdaptedTime::new)
                    .collect(Collectors.toList()));
        }
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Group toModelType() throws IllegalValueException {
        if (!Group.isValidGroup(groupName)) {
            throw new IllegalValueException("illegal value");
        }
        TimeIntervalList modelTimeIntervalListList = new TimeIntervalList();
        for (JsonAdaptedTime freeTime : meetingTimeList) {
            modelTimeIntervalListList.addTime(freeTime.toModelType());
        }

        return new Group(groupName, new GroupRemark(groupRemark), modelTimeIntervalListList);
    }

}
