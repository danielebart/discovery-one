package com.discoveryone.testutils

import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.annotations.ActivityNavigationDestination

@ActivityNavigationDestination
class ContainerTestActivity : AppCompatActivity(com.discoveryone.test.R.layout.container_layout)

@ActivityNavigationDestination
class EmptyTestActivity : AppCompatActivity()
