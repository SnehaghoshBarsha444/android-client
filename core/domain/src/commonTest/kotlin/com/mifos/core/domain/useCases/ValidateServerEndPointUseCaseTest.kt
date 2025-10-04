/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.domain.useCases

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateServerEndPointUseCaseTest {

    private val validateServerEndPoint = ValidateServerEndPointUseCase()

    @Test
    fun `valid domain names should pass validation`() = runTest {
        val validDomains = listOf(
            "example.com",
            "subdomain.example.com",
            "api.example.co.uk",
            "localhost",
            "mifos.community",
            "tt.mifos.community",
            "test-server.example.com",
            "192-168-1-1.nip.io",
        )

        validDomains.forEach { domain ->
            val result = validateServerEndPoint(domain)
            assertTrue(
                result.successful,
                "Domain '$domain' should be valid but was rejected: ${result.message}",
            )
        }
    }

    @Test
    fun `valid IPv4 addresses should pass validation`() = runTest {
        val validIPv4s = listOf(
            "127.0.0.1",
            "192.168.1.1",
            "10.0.0.1",
            "172.16.0.1",
            "8.8.8.8",
            "255.255.255.255",
            "0.0.0.0",
            "1.2.3.4",
        )

        validIPv4s.forEach { ip ->
            val result = validateServerEndPoint(ip)
            assertTrue(
                result.successful,
                "IPv4 address '$ip' should be valid but was rejected: ${result.message}",
            )
        }
    }

    @Test
    fun `valid IPv6 addresses should pass validation`() = runTest {
        val validIPv6s = listOf(
            "::1",
            "fe80::1",
            "2001:db8::1",
            "2001:0db8:0000:0000:0000:ff00:0042:8329",
            "2001:db8:0:0:1:0:0:1",
            "::ffff:192.0.2.1",
        )

        validIPv6s.forEach { ip ->
            val result = validateServerEndPoint(ip)
            assertTrue(
                result.successful,
                "IPv6 address '$ip' should be valid but was rejected: ${result.message}",
            )
        }
    }

    @Test
    fun `invalid IPv4 addresses should fail validation`() = runTest {
        val invalidIPv4s = listOf(
            "256.1.1.1",
            "1.1.1",
            "1.1.1.1.1",
            "192.168.1.256",
            "999.999.999.999",
            "a.b.c.d",
        )

        invalidIPv4s.forEach { ip ->
            val result = validateServerEndPoint(ip)
            assertFalse(
                result.successful,
                "IPv4 address '$ip' should be invalid but was accepted",
            )
        }
    }

    @Test
    fun `invalid domain names should fail validation`() = runTest {
        val invalidDomains = listOf(
            "",
            " ",
            "domain with spaces",
            "domain_with_underscore.com",
            ".example.com",
            "example.com.",
            "-example.com",
            "example-.com",
            "http://example.com",
            "https://example.com",
        )

        invalidDomains.forEach { domain ->
            val result = validateServerEndPoint(domain)
            assertFalse(
                result.successful,
                "Domain '$domain' should be invalid but was accepted",
            )
        }
    }

    @Test
    fun `blank endpoint should fail validation`() = runTest {
        val result = validateServerEndPoint("")
        assertFalse(result.successful, "Blank endpoint should fail validation")
    }
}
