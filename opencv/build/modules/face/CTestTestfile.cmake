# CMake generated Testfile for 
# Source directory: C:/Users/d_dan/Desktop/opencv_contrib-master/modules/face
# Build directory: C:/Users/d_dan/Desktop/opencv-master/build/modules/face
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
if("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
  add_test(opencv_test_face "C:/Users/d_dan/Desktop/opencv-master/build/bin/Debug/opencv_test_faced.exe" "--gtest_output=xml:opencv_test_face.xml")
  set_tests_properties(opencv_test_face PROPERTIES  LABELS "Extra;opencv_face;Accuracy" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/accuracy")
elseif("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
  add_test(opencv_test_face "C:/Users/d_dan/Desktop/opencv-master/build/bin/Release/opencv_test_face.exe" "--gtest_output=xml:opencv_test_face.xml")
  set_tests_properties(opencv_test_face PROPERTIES  LABELS "Extra;opencv_face;Accuracy" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/accuracy")
else()
  add_test(opencv_test_face NOT_AVAILABLE)
endif()
