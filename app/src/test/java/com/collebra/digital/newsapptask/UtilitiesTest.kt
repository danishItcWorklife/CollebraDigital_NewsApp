package com.collebra.digital.newsapptask

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import com.collebra.digital.newsapptask.utils.Utilities
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UtilitiesTest {

    private lateinit var context: Context
    private lateinit var biometricManager: BiometricManager
    private lateinit var keyguardManager: KeyguardManager

    @Before
    fun setUp() {
        context = mockk()
        biometricManager = mockk()
        keyguardManager = mockk()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { context.getSystemService(Context.KEYGUARD_SERVICE) } returns keyguardManager
    }

    @Test
    fun `isBiometricHardWareAvailable should return true when BIOMETRIC_SUCCESS for SDK `() {
        // Arrange
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_SUCCESS

        // Act
        val result = Utilities.isBiometricHardWareAvailable(context)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `isBiometricHardWareAvailable should return false when BIOMETRIC_ERROR_NO_HARDWARE`() {
        // Arrange
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE

        // Act
        val result = Utilities.isBiometricHardWareAvailable(context)

        // Assert
        assertEquals(false, result)
    }

    @Test
    fun `isBiometricHardWareAvailable should return true when BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED`() {
        // Arrange
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED

        // Act
        val result = Utilities.isBiometricHardWareAvailable(context)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `deviceHasPasswordPinLock should return true when keyguard is secure`() {
        // Arrange
        every { keyguardManager.isKeyguardSecure } returns true

        // Act
        val result = Utilities.deviceHasPasswordPinLock(context)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `deviceHasPasswordPinLock should return false when keyguard is not secure`() {
        // Arrange
        every { keyguardManager.isKeyguardSecure } returns false

        // Act
        val result = Utilities.deviceHasPasswordPinLock(context)

        // Assert
        assertEquals(false, result)
    }

    @Test
    fun `getStringArray should return correct string array`() {
        // Arrange
        val stringArray = arrayOf("string1", "string2", "string3")
        every { context.resources.getStringArray(any()) } returns stringArray

        // Act
        val result = Utilities.getStringArray(context, 0)

        // Assert
        assertEquals(listOf("string1", "string2", "string3"), result)
    }
}
