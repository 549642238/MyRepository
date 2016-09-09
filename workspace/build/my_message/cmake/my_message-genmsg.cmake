# generated from genmsg/cmake/pkg-genmsg.cmake.em

message(STATUS "my_message: 1 messages, 1 services")

set(MSG_I_FLAGS "-Imy_message:/home/freg/ros/workspace/src/my_message/msg;-Istd_msgs:/opt/ros/indigo/share/std_msgs/cmake/../msg")

# Find all generators
find_package(gencpp REQUIRED)
find_package(genlisp REQUIRED)
find_package(genpy REQUIRED)

add_custom_target(my_message_generate_messages ALL)

# verify that message/service dependencies have not changed since configure



get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/msg/Num.msg" NAME_WE)
add_custom_target(_my_message_generate_messages_check_deps_${_filename}
  COMMAND ${CATKIN_ENV} ${PYTHON_EXECUTABLE} ${GENMSG_CHECK_DEPS_SCRIPT} "my_message" "/home/freg/ros/workspace/src/my_message/msg/Num.msg" ""
)

get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv" NAME_WE)
add_custom_target(_my_message_generate_messages_check_deps_${_filename}
  COMMAND ${CATKIN_ENV} ${PYTHON_EXECUTABLE} ${GENMSG_CHECK_DEPS_SCRIPT} "my_message" "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv" ""
)

#
#  langs = gencpp;genlisp;genpy
#

### Section generating for lang: gencpp
### Generating Messages
_generate_msg_cpp(my_message
  "/home/freg/ros/workspace/src/my_message/msg/Num.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/my_message
)

### Generating Services
_generate_srv_cpp(my_message
  "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/my_message
)

### Generating Module File
_generate_module_cpp(my_message
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/my_message
  "${ALL_GEN_OUTPUT_FILES_cpp}"
)

add_custom_target(my_message_generate_messages_cpp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_cpp}
)
add_dependencies(my_message_generate_messages my_message_generate_messages_cpp)

# add dependencies to all check dependencies targets
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/msg/Num.msg" NAME_WE)
add_dependencies(my_message_generate_messages_cpp _my_message_generate_messages_check_deps_${_filename})
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv" NAME_WE)
add_dependencies(my_message_generate_messages_cpp _my_message_generate_messages_check_deps_${_filename})

# target for backward compatibility
add_custom_target(my_message_gencpp)
add_dependencies(my_message_gencpp my_message_generate_messages_cpp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS my_message_generate_messages_cpp)

### Section generating for lang: genlisp
### Generating Messages
_generate_msg_lisp(my_message
  "/home/freg/ros/workspace/src/my_message/msg/Num.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/my_message
)

### Generating Services
_generate_srv_lisp(my_message
  "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/my_message
)

### Generating Module File
_generate_module_lisp(my_message
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/my_message
  "${ALL_GEN_OUTPUT_FILES_lisp}"
)

add_custom_target(my_message_generate_messages_lisp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_lisp}
)
add_dependencies(my_message_generate_messages my_message_generate_messages_lisp)

# add dependencies to all check dependencies targets
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/msg/Num.msg" NAME_WE)
add_dependencies(my_message_generate_messages_lisp _my_message_generate_messages_check_deps_${_filename})
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv" NAME_WE)
add_dependencies(my_message_generate_messages_lisp _my_message_generate_messages_check_deps_${_filename})

# target for backward compatibility
add_custom_target(my_message_genlisp)
add_dependencies(my_message_genlisp my_message_generate_messages_lisp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS my_message_generate_messages_lisp)

### Section generating for lang: genpy
### Generating Messages
_generate_msg_py(my_message
  "/home/freg/ros/workspace/src/my_message/msg/Num.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message
)

### Generating Services
_generate_srv_py(my_message
  "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message
)

### Generating Module File
_generate_module_py(my_message
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message
  "${ALL_GEN_OUTPUT_FILES_py}"
)

add_custom_target(my_message_generate_messages_py
  DEPENDS ${ALL_GEN_OUTPUT_FILES_py}
)
add_dependencies(my_message_generate_messages my_message_generate_messages_py)

# add dependencies to all check dependencies targets
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/msg/Num.msg" NAME_WE)
add_dependencies(my_message_generate_messages_py _my_message_generate_messages_check_deps_${_filename})
get_filename_component(_filename "/home/freg/ros/workspace/src/my_message/srv/AddTwoInts.srv" NAME_WE)
add_dependencies(my_message_generate_messages_py _my_message_generate_messages_check_deps_${_filename})

# target for backward compatibility
add_custom_target(my_message_genpy)
add_dependencies(my_message_genpy my_message_generate_messages_py)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS my_message_generate_messages_py)



if(gencpp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/my_message)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/my_message
    DESTINATION ${gencpp_INSTALL_DIR}
  )
endif()
add_dependencies(my_message_generate_messages_cpp std_msgs_generate_messages_cpp)

if(genlisp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/my_message)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/my_message
    DESTINATION ${genlisp_INSTALL_DIR}
  )
endif()
add_dependencies(my_message_generate_messages_lisp std_msgs_generate_messages_lisp)

if(genpy_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message)
  install(CODE "execute_process(COMMAND \"/usr/bin/python\" -m compileall \"${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message\")")
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/my_message
    DESTINATION ${genpy_INSTALL_DIR}
  )
endif()
add_dependencies(my_message_generate_messages_py std_msgs_generate_messages_py)
