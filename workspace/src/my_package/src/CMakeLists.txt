## Declare a C++ executable
add_executable(talker talker.cpp)
 
## Specify libraries to link a library or executable target against
target_link_libraries(talker
	${catkin_LIBRARIES}
)

## Declare a C++ executable
add_executable(listener listener.cpp)
 
## Specify libraries to link a library or executable target against
target_link_libraries(listener
	${catkin_LIBRARIES}
)

## Declare a C++ executable
add_executable(add_two_ints_server add_two_ints_server.cpp)
 
## Specify libraries to link a library or executable target against
target_link_libraries(add_two_ints_server
	${catkin_LIBRARIES}
)

## Declare a C++ executable
add_executable(add_two_ints_client add_two_ints_client.cpp)
 
## Specify libraries to link a library or executable target against
target_link_libraries(add_two_ints_client
	${catkin_LIBRARIES}
)

## Declare a C++ executable
add_executable(he he.cpp)
 
## Specify libraries to link a library or executable target against
target_link_libraries(he
	${catkin_LIBRARIES} libhello.a
)
