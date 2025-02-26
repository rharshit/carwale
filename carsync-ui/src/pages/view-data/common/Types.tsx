
export interface CarModel {
    id: string
    client: string
    clientId: string
    city: string
    make: string
    model: string
    variant: string
    year: number
    price: number
    mileage: number
    url: string
    imageUrls: string[]
    specs: Specs
}

export interface Specs {
    fuelType?: string
    transmissionType?: string
    engineType?: string
    engineDisplacement?: number
    enginePower?: number
    engineTorque?: number
    length?: number
    width?: number
    height?: number
    wheelbase?: number
    groundClearance?: number
    kerbWeight?: number
    bootSpace?: number
    drivetrain?: string
}
