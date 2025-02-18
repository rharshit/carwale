/* eslint-disable @typescript-eslint/no-unused-vars */
import { Button } from "antd";

type FilterProps = {
    onApplyFilter: (carFilter: CarFilter) => void
}

export function FilterComponent(filterProps: FilterProps) {
    const { onApplyFilter } = filterProps

    //TODO: Populate the filter
    function createFilter(): void {
        const filter: CarFilter = {}
        onApplyFilter(filter)
    }

    return (
        <>
            <span>Filter component</span>
            <Button onClick={e => createFilter()}>Apply</Button>
        </>
    );
}

export type CarFilter = {
    limit?: number,
    skip?: number,
    cities?: string[],
    makes?: string[],
    models?: string[],
    variants?: string[],
    minYear?: number,
    maxYear?: number,
    minPrice?: number,
    maxPrice?: number,
    minMileage?: number,
    maxMileage?: number,
    minPower?: number,
    maxPower?: number,
    minTorque?: number,
    maxTorque?: number,
    minLength?: number,
    maxLength?: number,
    minWidth?: number,
    maxWidth?: number,
    minHeight?: number,
    maxHeight?: number,
    minWheelbase?: number,
    maxWheelbase?: number,
}