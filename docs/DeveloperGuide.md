---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# ProjectPRO Developer Guide

<!-- * Table of Contents -->
<page-nav-print />
--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S1-CS2103T-T10-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Delete feature

#### Implementation

The proposed delete person/group mechanism is facilitated by `AddressBook`. It implements the following operations:

* `AddressBook#removePerson(Person p)` — Removes Person p from the address book.
* `AddressBook#removeGroup(Group g)` — Remove Group g from the address book.

These operations are exposed in the `Model` interface as `Model#deletePerson()`, `Model#deleteGroup()` respectively.

##### Delete Person

Given below is an example usage scenario and how the Delete Person mechanism behaves at each step.

Step 1. The user executes `delete n/Alex Yeoh` command to delete a person named 'Alex Yeoh' in the address book. After parsing, a new `DeletePersonCommand` object will be returned.

Step 2. `DeletePersonCommand` is executed, in which `Model#deletePerson()` is called, removing the Person with name 'Alex Yeoh' from the address book while returning the `Person` object.

<box type="info" seamless>

**Note:** If no such person named 'Alex Yeoh' exists, a `CommandException` will be thrown.

</box>

Step 3. `Person#getGroups()` is called to obtain a `GroupList` of groups the target person is part of.

Step 4. The `GroupList` is converted into a stream, where each element is a `Group`. `Group#removePerson()` is called for each Group in the stream, removing the target Person from all `ObserverableList<Person> listOfGroupMates` in `Group`.

The following sequence diagram shows how the Delete Person operation works:

<puml src="diagrams/DeletePersonSequenceDiagram.puml" alt="DeletePersonSequence" />

<box type="info" seamless>

**Note:** The lifeline for `DeletePersonCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

##### Delete Group

Given below is an example usage scenario and how the Delete Group mechanism behaves at each step.

Step 1. The user executes `delete g/CS2100` command to delete a group named 'CS2100' in the address book. After parsing, a new `DeleteGroupCommand` object will be returned.

Step 2. `DeleteGroupCommand` is executed, in which `Model#deleteGroup()` is called, removing the `Group` with name 'CS2100' (the target group) from the address book while returning the `Group` object.

<box type="info" seamless>

**Note:** If no such group named 'CS2100' exists, a `CommandException` will be thrown.

</box>

Step 3. `Group#getListOfGroupMates()` is called to obtain a `ObservableList<Person>` of Persons that are a part of the target group.

Step 4. The `ObservableList<Person>` is converted into a stream, where each element is a `Person`. `Person#removeGroup()` is called for each Person in the stream, removing the target Group from all `GroupList groups` in `Person`.

Step 5. `DeleteGroupCommand` creates a new `CommandResult` with the corresponding message, and returns the result to the `LogicManager`.

The following sequence diagram shows how the Delete Group operation works:

<puml src="diagrams/DeleteGroupSequenceDiagram.puml" alt="DeleteGroupSequence" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteGroupCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>


### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how the undo operation works:

<puml src="diagrams/UndoSequenceDiagram.puml" alt="UndoSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Proposed Group Remark Feature

#### Proposed Implementation

The proposed group remark feature is facilitated by the `Group` class. It includes a `Group Remark` field and implements the `Group#setGroupRemark()` operation. This feature is exposed in the `Model` interface as `Model#addGroupRemark()`.

Here's an example usage scenario and how the group remark mechanism behaves at each step:

**Step 1.** The user creates a group called `CS2103T`. The `Group` is initialized with an empty `groupRemark`.

**Step 2.** The user executes the `remark g/CS2103T r/Quiz tomorrow` command to add the remark "Quiz tomorrow" to the `CS2103T` group. The `GroupRemarkCommandParser` extracts the group and remark from the input and creates a `GroupRemarkCommand`, which calls `Model#addGroupRemark(groupName, groupRemark)`. The model retrieves the existing `CS2103T` group from the database and calls the group's `Group#setRemark(groupRemark)`, adding the `groupRemark` to the group.

**Note:** If the user wants to modify the group remark, they can execute the same command with the new remark. The existing remark will be deleted and overwritten, and the new remark is stored in the group.

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/GroupRemarkSequenceDiagram.puml" alt="GroupRemarkSequenceDiagram" />

#### Design Considerations

**Aspects:**

- **Alternative 1 (current choice):** Overrides original remark
    - Pros: Easy to implement.
    - Cons: May be troublesome if the user wants to keep contents from the original remark.
- **Alternative 2:** Edits original remark
    - Pros: Easy to add more information.
    - Cons: Could be confusing to edit if there are many changes.


### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


### Add Command Feature

The add feature is facilitate by a number of classes such as `Person` and `Model` 

Step 1. The user launches the application for the first time.

Step 2. The user executes `“add n/John Doe p/98765432 e/johnd@example.com g/CS2103T”` command to add a new person. `LogicManager#execute` is called which then calls `AddressBookParser#parseCommand` to decide on the type of command. `AddressBookParse`r` then calls `AddCommandParser`,

Step 3, The `AddCommandParser` is called to read the user input. `AddCommandParser` calls `ArgumentTokenizer#tokenize` to check the prefixes of the user input. `AddCommandParser` then calls `ArgumentMultimap#getValue()` to get inputs after each prefix.
The result of it is then passed to `ParserUtil#parse{Attribute}` methods to parse each attributes such as `Name`. `AddCommandParser` then makes new person object. `AddCommandParser` then calls `AddCommand` and passes `Person` inside.

Step.4 `AddCommand` then calls `Model#addPerson()` which then calls `AddressBook#addPerson()`. The latter method will add person inside the `uniquePersonList` in `addressBook`. `AddCommand` also calls `Model#addGroup` which then calls `AddressBook#addGroup` to add the group inside `grouplist` if the group does not exist.
Lastly, `AddCommand` adds the person inside the group

Note: No duplication is allowed in addressbook for most of Person’s attribute (name, email and phone number.)

<puml src="diagrams/AddCommandSequenceDiagram.puml" alt="AddCommandSeqDiagram" />

#### Design consideration:

**Aspect: Handling group attribute in user input**

* **Alternative 1 (Current Choice):** Only allow user to add one group for each `Add` Command
  * Pros: Conveniently adds a person into group while creating a new contact at the same time
  * Cons: User input is relatively longer
* **Alternative 2:** Allow user to add as many groups as required for each `Add` Command
  * Pros: Conveniently adds a person into group while creating a new contact at the same time
  * Cons: User input can get potentially very long, increasing the chance of invalid input

### [Proposed] Delete Time Feature

#### Proposed Implementation

The proposed delete time feature is facilitated by the `timeIntervalList` and `Person` class. It accesses the `timeIntervalList` from the `Person` class and deletes a time interval with `Person#deleteFreeTime()`. The operation is exposed in the `Model` interface as `Model#deleteTimeFromPerson`.

Step 1. The user launches the application. The `AddressBook` will be initialized with the free time of its contacts.

Step 2. The user executes the command `deleteTime n/Alex Yeoh t/mon 1200 - mon 1400 ;tue 1000 - wed 1600`. The `deleteTimeCommandParser` will be called to parse the inputs and call the `deletePersonTimeCommand`. The `deletePersonTime` command calls `Model#deleteTimeFromPerson()`, which will call `Person#deleteFreeTime()`.

**Note:** Since multiple inputs are allowed, an array list of time intervals are passed around, each of which is to be deleted.

Step 3. The function will be called in the person's `timeInterval` list. The application will loop through each time interval that is to be deleted and in the person's `timeInterval` list. Each time interval will be compared to see whether the `timeIntervalList` contains the time interval to be deleted. Afterwards, the new `timeInterval` list will be saved.

**Note:** If a time interval is not in the person's list, that interval will be collated and printed to specifically notify the user which time intervals are not in the list. The other time intervals that are in the list will still be deleted.

Similarly, the group command does the same, except for the `Group` class.

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/DeletePersonTimeDiagram.puml" alt="DeletePersonTimeDiagram"/>

#### Design Considerations

**Aspect: Error Messages**

* Alternative 1 (current choice): Print specific error messages.
  Pros: Allow users to understand which inputs went wrong.
  Cons: May have performance issues in terms of runtime as more functions will be used to craft the error message. Currently, we utilized a `StringBuilder` to craft the message and did extra checks to see whether there had been any errors appended to the error message.

* Alternative 2: Generalized error message.
  Pros: Will be faster to implement.
  Cons: User might be unsure why the function went wrong.

**Aspect: How to Handle Multiple Time Inputs**

* Alternative 1 (current choice): Parse each time input one by one and execute the commands.
  Pros: More user-friendly and efficient as users can delete more time intervals at once.
  Cons: More expensive as more functions will be called to parse the inputs.

* Alternative 2: Allow only single input.
  Pros: Faster as fewer functions are called.
  Cons: Not as user-friendly since users will have to delete time intervals one by one.

**Aspect: How to Handle Errors in Time Intervals**

* Alternative 1 (current choice): Delete the time intervals that are correct and return the intervals that are wrong.
  Pros: Better user experience as users need not rewrite intervals that were right.
  Cons: Increased memory usage to store the errors.

* Alternative 2: Do not carry out the delete at all.
  Pros: More time and memory efficient.
  Cons: Not as user-friendly since users will have to delete time intervals that were originally correct, wasting their time.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* University students often spend a lot of time coordinating project meetup
* sessions and waiting for replies and they are not aware of one another’s schedules.
* This app can help to save time by listing available time slots of individuals in a team.


**Value proposition**: Text-friendly project management tool that helps students schedule meetings with different groups while also keeping track of tasks and 
responsibilities of each member. Our app will track the schedule of each contact and tasks individuals have to do for their project.


### User stories

| Priority | As a ...                                    | I want to ...                | So that I can ...                                                      |
|----------|--------------------------------------------|------------------------------|------------------------------------------------------------------------|
| `***`    | student                                    | add a new contact            | keep track of any new contacts                                         |
| `***`    | student with many contacts                 | organize contacts into groups | easily keep track and manage my contacts                               |
| `***`    | student with many team members             | record team members' info     | keep track of my team members' contact information                     |
| `***`    | student with many projects                 | delete a group               | avoid clutter and unnecessary attention to completed projects          |
| `***`    | student with many contacts                 | search for group members     | quickly access contact details using name, contact number              |
| `***`    | user                                       | save entered information      | avoid repetitive data entry                                            |
| `**`     | team leader                                | add tasks to contacts        | remember who is responsible for which task                             |
| `**`     | student                                    | filter contacts by project   | easily view tasks for a specific project group                         |
| `**`     | user                                       | prioritize tasks             | work on important tasks first                                          |
| `**`     | student                                    | add time slots of group mates| find a suitable meeting time when everyone is available                |
| `**`     | new user                                   | access a help command        | quickly learn about application functions without reading a long guide |
| `*`      | impatient user                             | access the user guide        | quickly learn how to use the application                               |
| `*`      | technology-challenged student              | read the user guide          | gain a better understanding of how to use the application              |
| `*`      | fast but inaccurate typer                  | undo a previous command      | correct typing mistakes                                                |
| `*`      | forgetful student                          | add a reminder               | ensure attendance at upcoming project meetings                         |
| `*`      | student with many projects                 | color code projects          | differentiate between various project groups                           |
| `*`      | lazy user                                  | minimize typing/clicking     | achieve tasks with minimal effort                                      |
| `*`      | student with an irregular schedule         | edit contact information     | easily manage changes in contact details                               |
| `*`      | user                                       | filter contacts by courses   | view contacts based on shared courses or projects                      |
| `*`      | user                                       | upload attachments/files     | improve collaboration and reference for tasks and projects             |
| `*`      | user                                       | view contact profiles        | access course schedules, contact details, and profile pictures         |

### Use cases

(For all use cases below, the **System** is `ProjectPRO` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Creating contact**

**MSS**

1. User requests to add contact
2. ProjectPRO adds new contact
3. User gets back success command result 

   Use Case ends.

**Extensions:**
* 1a.  System detects error in input
    * 1a1.  User gets error message
    * Use case repeats from step 1

* 1b. System detects duplicate contact requested by user.
    * 1b1. System displays corresponding error message. 
    * Use case ends.

    
**Use case: Delete contact**

**MSS**

1. User requests to delete contact 
2. System deletes contact. 
3. System produces a success message.

   Use Case ends

**Extensions**
* 1a. System detects incorrect data entered. 
    * 1a1. System requests for the correct data. 
    * 1a2. User enters new data 
    * 1a3. Steps 1a1-1a2 are repeated until the data entered are correct.
    * Use case resumes from step 2.

**Use case: Add contact to group**

**MSS**

1. User requests to add a contact into a group. 
2. System adds user into the group successfully. 
3. System displays a success message.

   Use Case ends

**Extensions**
* 1a. System detects an error in the data entered.
    * 1a1. System requests for the correct data. 
    * 1a2. User enters new data 
    * 1a3. Steps 1a1-1a2 are repeated until the data entered are correct. 
    * Use case resumes from step 2.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 11 or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be able to save the state of the user’s actions.
5. Should be able to handle an increasing number of contacts and events without a significant degradation in performance.
6. Data loss or corruption should not occur, even in the event of unexpected crashes or system failures.
7. Should not support any online features. No user actions or data manipulation should require online features.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Java 11**: Programming language
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
