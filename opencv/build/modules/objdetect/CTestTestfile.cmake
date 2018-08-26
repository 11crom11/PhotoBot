# CMake generated Testfile for 
# Source directory: C:/Users/d_dan/Desktop/opencv-master/modules/objdetect
# Build directory: C:/Users/d_dan/Desktop/opencv-master/build/modules/objdetect
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
if("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
  add_test(opencv_test_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Debug/opencv_test_objdetectd.exe" "--gtest_output=xml:opencv_test_objdetect.xml")
  set_tests_properties(opencv_test_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Accuracy" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/accuracy")
elseif("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
  add_test(opencv_test_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Release/opencv_test_objdetect.exe" "--gtest_output=xml:opencv_test_objdetect.xml")
  set_tests_properties(opencv_test_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Accuracy" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/accuracy")
else()
  add_test(opencv_test_objdetect NOT_AVAILABLE)
endif()
if("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
  add_test(opencv_perf_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Debug/opencv_perf_objdetectd.exe" "--gtest_output=xml:opencv_perf_objdetect.xml")
  set_tests_properties(opencv_perf_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Performance" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/performance")
elseif("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
  add_test(opencv_perf_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Release/opencv_perf_objdetect.exe" "--gtest_output=xml:opencv_perf_objdetect.xml")
  set_tests_properties(opencv_perf_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Performance" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/performance")
else()
  add_test(opencv_perf_objdetect NOT_AVAILABLE)
endif()
if("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
  add_test(opencv_sanity_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Debug/opencv_perf_objdetectd.exe" "--gtest_output=xml:opencv_perf_objdetect.xml" "--perf_min_samples=1" "--perf_force_samples=1" "--perf_verify_sanity")
  set_tests_properties(opencv_sanity_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Sanity" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/sanity")
elseif("${CTEST_CONFIGURATION_TYPE}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
  add_test(opencv_sanity_objdetect "C:/Users/d_dan/Desktop/opencv-master/build/bin/Release/opencv_perf_objdetect.exe" "--gtest_output=xml:opencv_perf_objdetect.xml" "--perf_min_samples=1" "--perf_force_samples=1" "--perf_verify_sanity")
  set_tests_properties(opencv_sanity_objdetect PROPERTIES  LABELS "Main;opencv_objdetect;Sanity" WORKING_DIRECTORY "C:/Users/d_dan/Desktop/opencv-master/build/test-reports/sanity")
else()
  add_test(opencv_sanity_objdetect NOT_AVAILABLE)
endif()
