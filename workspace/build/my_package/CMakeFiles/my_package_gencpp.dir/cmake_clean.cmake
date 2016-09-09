FILE(REMOVE_RECURSE
  "CMakeFiles/my_package_gencpp"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/my_package_gencpp.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
