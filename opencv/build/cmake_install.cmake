# Install script for directory: C:/Users/d_dan/Desktop/opencv-master

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "C:/Users/d_dan/Desktop/opencv-master/build/install")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Release")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "FALSE")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippicv-readme.htm" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/readme.htm")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippicv-ippEULA.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/license/ippEULA.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/lib/intel64/ippicvmt.lib")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippiw-EULA.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/../ippiw_win/EULA.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippiw-redist.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/../ippiw_win/redist.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippiw-support.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/../ippiw_win/support.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "ippiw-third-party-programs.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippicv/ippicv_win/../ippiw_win/third-party-programs.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlicensesx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/etc/licenses" TYPE FILE OPTIONAL RENAME "opencl-headers-LICENSE.txt" FILES "C:/Users/d_dan/Desktop/opencv-master/3rdparty/include/opencl/LICENSE.txt")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/opencv2" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/cvconfig.h")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/opencv2" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/opencv2/opencv_modules.hpp")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  if(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib/OpenCVModules.cmake")
    file(DIFFERENT EXPORT_FILE_CHANGED FILES
         "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib/OpenCVModules.cmake"
         "C:/Users/d_dan/Desktop/opencv-master/build/CMakeFiles/Export/x64/vc15/staticlib/OpenCVModules.cmake")
    if(EXPORT_FILE_CHANGED)
      file(GLOB OLD_CONFIG_FILES "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib/OpenCVModules-*.cmake")
      if(OLD_CONFIG_FILES)
        message(STATUS "Old export file \"$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib/OpenCVModules.cmake\" will be replaced.  Removing files [${OLD_CONFIG_FILES}].")
        file(REMOVE ${OLD_CONFIG_FILES})
      endif()
    endif()
  endif()
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/CMakeFiles/Export/x64/vc15/staticlib/OpenCVModules.cmake")
  if("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/CMakeFiles/Export/x64/vc15/staticlib/OpenCVModules-debug.cmake")
  endif()
  if("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib" TYPE FILE FILES "C:/Users/d_dan/Desktop/opencv-master/build/CMakeFiles/Export/x64/vc15/staticlib/OpenCVModules-release.cmake")
  endif()
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/x64/vc15/staticlib" TYPE FILE FILES
    "C:/Users/d_dan/Desktop/opencv-master/build/win-install/OpenCVConfig-version.cmake"
    "C:/Users/d_dan/Desktop/opencv-master/build/win-install/x64/vc15/staticlib/OpenCVConfig.cmake"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xdevx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/." TYPE FILE FILES
    "C:/Users/d_dan/Desktop/opencv-master/build/win-install/OpenCVConfig-version.cmake"
    "C:/Users/d_dan/Desktop/opencv-master/build/win-install/OpenCVConfig.cmake"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xlibsx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/." TYPE FILE PERMISSIONS OWNER_READ GROUP_READ WORLD_READ FILES "C:/Users/d_dan/Desktop/opencv-master/LICENSE")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for each subdirectory.
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/zlib/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/libjpeg-turbo/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/libtiff/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/libwebp/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/libjasper/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/libpng/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/openexr/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ippiw/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/protobuf/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/3rdparty/ittnotify/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/include/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/modules/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/doc/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/data/cmake_install.cmake")
  include("C:/Users/d_dan/Desktop/opencv-master/build/apps/cmake_install.cmake")

endif()

if(CMAKE_INSTALL_COMPONENT)
  set(CMAKE_INSTALL_MANIFEST "install_manifest_${CMAKE_INSTALL_COMPONENT}.txt")
else()
  set(CMAKE_INSTALL_MANIFEST "install_manifest.txt")
endif()

string(REPLACE ";" "\n" CMAKE_INSTALL_MANIFEST_CONTENT
       "${CMAKE_INSTALL_MANIFEST_FILES}")
file(WRITE "C:/Users/d_dan/Desktop/opencv-master/build/${CMAKE_INSTALL_MANIFEST}"
     "${CMAKE_INSTALL_MANIFEST_CONTENT}")
