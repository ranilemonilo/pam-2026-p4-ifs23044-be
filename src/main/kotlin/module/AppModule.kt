package org.delcom.module

import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.PlantRepository
import org.delcom.repositories.IHandphoneRepository
import org.delcom.repositories.HandphoneRepository
import org.delcom.services.PlantService
import org.delcom.services.HandphoneService
import org.delcom.services.ProfileService
import org.koin.dsl.module

val appModule = module {

    // Plant Repository
    single<IPlantRepository> {
        PlantRepository()
    }

    // Plant Service
    single {
        PlantService(get())
    }

    // Handphone Repository
    single<IHandphoneRepository> {
        HandphoneRepository()
    }

    // Handphone Service
    single {
        HandphoneService(get())
    }

    // Profile Service
    single {
        ProfileService()
    }
}