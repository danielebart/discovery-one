package com.discoveryone.testutils

import androidx.appcompat.app.AppCompatActivity
import com.discoveryone.annotations.ActivityRoute

@ActivityRoute
class ContainerTestActivity : AppCompatActivity(com.discoveryone.test.R.layout.container_layout)

@ActivityRoute
class EmptyTestActivity : AppCompatActivity()
