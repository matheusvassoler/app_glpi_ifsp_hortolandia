package com.glpi.ifsp.hortolandia

import io.mockk.MockKAnnotations
import org.junit.Before

open class BaseUnitTest {

    @Before
    fun setup() = MockKAnnotations.init(this, relaxed = true)
}
