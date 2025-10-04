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

import com.mifos.core.domain.utils.ValidationResult
import core.domain.generated.resources.Res
import core.domain.generated.resources.core_domain_error_endpoint_blank
import core.domain.generated.resources.core_domain_error_endpoint_invalid
import org.jetbrains.compose.resources.getString

class ValidateServerEndPointUseCase {
    suspend operator fun invoke(endPoint: String): ValidationResult {
        // Regex for validating IPv4 addresses
        val ipv4Regex =
            Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")

        // Regex for validating IPv6 addresses (simplified but covers common cases)
        val ipv6Regex =
            Regex("^(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|::([fF]{4}(:0{1,4})?:)?((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))$")

        // Regex for validating domain names
        val domainRegex =
            Regex("^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$")

        if (endPoint.isBlank()) {
            return ValidationResult(false, getString(Res.string.core_domain_error_endpoint_blank))
        }

        // Check if it contains a colon (likely IPv6)
        if (endPoint.contains(':')) {
            return if (ipv6Regex.matches(endPoint)) {
                ValidationResult(true)
            } else {
                ValidationResult(false, getString(Res.string.core_domain_error_endpoint_invalid))
            }
        }

        // Check if it looks like an IPv4 address (only digits and dots)
        if (endPoint.matches(Regex("^[0-9.]+$"))) {
            return if (ipv4Regex.matches(endPoint)) {
                ValidationResult(true)
            } else {
                ValidationResult(false, getString(Res.string.core_domain_error_endpoint_invalid))
            }
        }

        // Otherwise, validate as a domain name
        return if (domainRegex.matches(endPoint)) {
            ValidationResult(true)
        } else {
            ValidationResult(false, getString(Res.string.core_domain_error_endpoint_invalid))
        }
    }
}
