import {
    DotChartOutlined,
    GroupOutlined,
    LinkOutlined,
    UngroupOutlined,
} from '@ant-design/icons';
import { Flex, Radio, RadioChangeEvent, Table, TableColumnsType, Tooltip } from "antd";
import { RenderedCell } from 'rc-table/lib/interface';
import React, { useEffect, useState } from "react";
import { CarModel } from "../common/Types";

type AllCarListProps = {
    allCars: CarModel[],
    loading: boolean
}

type TableCarData = {
    key: React.ReactNode,
    name: string,
    city?: string,
    make?: string,
    model?: string,
    variant?: string,
    url?: string,
    year: (number | null)[],
    price: (number | null)[],
    power: (number | null)[],
    torque: (number | null)[],
    displacement: (number | null)[],
    children?: TableCarData[]
}

export function AllCarsList(allCarListProps: AllCarListProps) {
    const {
        allCars,
        loading
    } = allCarListProps

    const [listView, setListView] = useState<string>('group')
    const [filteredCars, setFilteredCars] = useState<TableCarData[]>([])
    const [filteredCarsTree, setFilteredCarsTree] = useState<TableCarData[]>([])

    const isList = (): boolean => {
        return listView == 'list';
    }

    const onChangeListView = (e: RadioChangeEvent) => {
        setListView(e.target.value);
    };

    const defaultTextRenderer = (element: React.ReactNode): React.ReactNode | RenderedCell<TableCarData> => {
        return element
    }

    const defaultRangeRenderer = (value: (number | null)[]): React.ReactNode | RenderedCell<TableCarData> => {
        let text = ''
        if (value == null) {
            text = ''
        } else if (value.length == 0) {
            text = ''
        } else {
            if (value.length == 1) {
                text = '' + (value[0] ?? '')
            } else if (value.length == 2) {
                if (value[0] == null) {
                    if (value[1] == null) {
                        text = ''
                    } else {
                        text = '' + (value[1] ?? '')
                    }
                } else {
                    if (value[1] == null) {
                        text = '' + (value[0] ?? '')
                    } else {
                        if (value[0] == value[1]) {
                            text = '' + (value[0] ?? '')
                        } else {
                            text = value.join(' - ')
                        }
                    }
                }
            } else {
                text = value.join(' - ')
            }
        }
        return defaultTextRenderer(text);
    }

    //TODO: Add filter condition in this function
    const filterCar = (car: CarModel): boolean => {
        return true;
    }

    function updateFilteredCars(allCars: CarModel[]) {
        setFilteredCars(allCars
            .filter(car => filterCar(car))
            .map(car => {
                return {
                    key: car.id,
                    name: [car.make, car.model, car.variant].join(' '),
                    city: car.city,
                    make: car.make,
                    model: car.model,
                    variant: car.variant,
                    url: car.url,
                    year: [car.year, car.year],
                    price: [car.price, car.price],
                    power: [car.specs.enginePower, car.specs.enginePower],
                    torque: [car.specs.engineTorque, car.specs.engineTorque],
                    displacement: [car.specs.engineDisplacement, car.specs.engineDisplacement]
                } as TableCarData
            }))
    }

    function updateMinMax(group: TableCarData, car: TableCarData) {
        const computeNewMinMax = (a: (number | null)[], b: (number | null)[]): (number | null)[] => {
            const a0 = a[0];
            const a1 = a[1];
            const b0 = b[1];
            const b1 = b[1];
            const fin0 = a0 == null ? b0 : (b0 == null ? a0 : (a0 < b0 ? a0 : b0))
            const fin1 = a1 == null ? b1 : (b1 == null ? a1 : (a1 > b1 ? a1 : b1))
            return [fin0, fin1]
        }
        group.year = computeNewMinMax(group.year, car.year)
        group.price = computeNewMinMax(group.price, car.price)
        group.power = computeNewMinMax(group.power, car.power)
        group.torque = computeNewMinMax(group.torque, car.torque)
        group.displacement = computeNewMinMax(group.displacement, car.displacement)
    }

    function updateTreeData(filteredCars: TableCarData[]) {
        const makes: TableCarData[] = [];
        filteredCars.forEach(car => {
            let make = makes.filter(make => { return make.make == car.make })[0]
            if (make == null) {
                make = {
                    key: car.make,
                    name: car.make ?? '',
                    make: car.make,
                    year: car.year,
                    price: car.price,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    children: []
                } as TableCarData;
                makes.push(make)
            }
            updateMinMax(make, car);

            const models: TableCarData[] = make.children ?? [];
            let model = models.filter(model => { return model.model == car.model })[0]
            if (model == null) {
                model = {
                    key: car.make + "/" + car.model,
                    name: car.model,
                    make: car.make,
                    model: car.model,
                    year: car.year,
                    price: car.price,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    children: []
                } as TableCarData;
                models.push(model)
            }
            updateMinMax(model, car)
            make.children = models;

            const variants: TableCarData[] = model.children ?? [];
            let variant = variants.filter(variant => { return variant.variant == car.variant })[0]
            if (variant == null) {
                variant = {
                    key: car.make + "/" + car.model + "/" + car.variant,
                    name: car.variant,
                    make: car.make,
                    model: car.model,
                    variant: car.variant,
                    year: car.year,
                    price: car.price,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    children: []
                } as TableCarData;
                variants.push(variant)
            }
            updateMinMax(variant, car);
            model.children = variants;

            variant.children?.push(car)
        })
        setFilteredCarsTree(makes)
    }

    useEffect(() => {
        updateTreeData(filteredCars)
    }, [filteredCars])

    useEffect(() => {
        updateFilteredCars(allCars)
    }, [allCars])

    const columns: TableColumnsType<TableCarData> = [
        {
            title: 'Name',
            key: 'name',
            render: (value, record, index) => {
                return <>
                    {defaultTextRenderer(record.name)}
                    {(
                        record.url == null ? '' : <a href={record.url} target="_blank" rel="noopener noreferrer"> <LinkOutlined /></a>
                    )}
                </>
            }
        },
        {
            title: 'City',
            key: 'city',
            render: (value, record, index) => {
                return defaultTextRenderer(record.city ?? '')
            }
        },
        {
            title: 'Year',
            key: 'year',
            render: (value, record, index) => {
                return defaultRangeRenderer(record.year)
            }
        },
        {
            title: 'Price',
            key: 'price',
            render: (value, record, index) => {
                return defaultRangeRenderer(record.price)
            }
        },
        {
            title: 'Power',
            key: 'power',
            render: (value, record, index) => {
                return defaultRangeRenderer(record.power)
            }
        },
        {
            title: 'Torque',
            key: 'torque',
            render: (value, record, index) => {
                return defaultRangeRenderer(record.torque)
            }
        },
        {
            title: 'Displacement',
            key: 'displacement',
            render: (value, record, index) => {
                return defaultRangeRenderer(record.displacement)
            }
        },
    ]

    return <>
        <Flex vertical>
            <Radio.Group
                onChange={onChangeListView}
                value={listView}
                optionType="button"
                options={[
                    {
                        value: 'list',
                        label: (<Tooltip title='List'>
                            <UngroupOutlined style={{ fontSize: 18 }} />
                        </Tooltip>
                        ),
                    },
                    {
                        value: 'group',
                        label: (<Tooltip title='Group'>
                            <GroupOutlined style={{ fontSize: 18 }} />
                        </Tooltip>

                        ),
                    },
                    {
                        value: 'graph',
                        label: (<Tooltip title='Graph'>
                            <DotChartOutlined style={{ fontSize: 18 }} />
                        </Tooltip>
                        ),
                    }
                ]}
            />
            <Table<TableCarData>
                columns={columns}
                dataSource={isList() ? filteredCars : filteredCarsTree}
                loading={loading}
            />
        </Flex>
    </>
}

export default AllCarsList;

