import {
    DotChartOutlined,
    GroupOutlined,
    LinkOutlined,
    UngroupOutlined,
} from '@ant-design/icons';
import { ConfigProvider, Flex, Input, Radio, RadioChangeEvent, Table, TableColumnsType, Tag, theme, Tooltip, Typography } from "antd";
import { SortOrder } from 'antd/es/table/interface';
import { RenderedCell } from 'rc-table/lib/interface';
import React, { Key, useEffect, useState } from "react";
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
    mileage: (number | null)[],
    power: (number | null)[],
    torque: (number | null)[],
    displacement: (number | null)[],
    length: (number | null)[],
    width: (number | null)[],
    height: (number | null)[],
    wheelbase: (number | null)[],
    children?: TableCarData[]
}

export function AllCarsList(allCarListProps: AllCarListProps) {
    const {
        allCars,
        loading
    } = allCarListProps

    const { Text } = Typography;

    const {
        token: { colorBgContainer, colorSuccessBg, colorErrorBg },
    } = theme.useToken();

    const [listView, setListView] = useState<string>('group')
    const [filteredCars, setFilteredCars] = useState<TableCarData[]>([])
    const [filteredCarsTree, setFilteredCarsTree] = useState<TableCarData[]>([])
    const [nameFilter, setNameFilter] = useState<string>('')
    const [isAscend, setAscend] = useState<boolean>(true)

    const isList = (): boolean => {
        return listView == 'list';
    }

    const onChangeListView = (e: RadioChangeEvent) => {
        setListView(e.target.value);
    };

    const defaultTextRenderer = (element: React.ReactNode, disabled: boolean = false): React.ReactNode | RenderedCell<TableCarData> => {
        return <Text disabled={disabled}>{element}</Text>
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

    const defaultTextSorter = (a: string | null | undefined, b: string | null | undefined, sortOrder: SortOrder | undefined): number => {
        setAscend(sortOrder == 'ascend');
        if (a == null) {
            return -1
        } else if (b == null) {
            return 1
        } else {
            return a.localeCompare(b);
        }
    }

    const getMinValue = (num: (number | null)[]): number | null => {
        if (num.length == 0) {
            return null;
        } else if (num.length == 1) {
            return num[0];
        } else if (num.length == 2) {
            return num[0] == null ? num[1] : num[0]
        } else {
            throw new Error("Cant get min value");
        }
    }
    const getMaxValue = (num: (number | null)[]): number | null => {
        if (num.length == 0) {
            return null;
        } else if (num.length == 1) {
            return num[0];
        } else if (num.length == 2) {
            return num[1] == null ? num[0] : num[1]
        } else {
            throw new Error("Cant get max value");
        }
    }

    const defaultRangeSorter = (a: (number | null)[], b: (number | null)[], sortOrder: SortOrder | undefined): number => {
        setAscend(sortOrder == 'ascend');
        if (sortOrder == null) {
            return 0;
        } else {
            if (a == null) {
                return -1
            } else if (b == null) {
                return 1
            } else {
                const [aMin, aMax, bMin, bMax] = [getMinValue(a), getMaxValue(a), getMinValue(b), getMaxValue(b)]
                if (sortOrder == 'descend') {
                    if (aMax == null) {
                        return bMax == null ? 0 : -1;
                    } else if (bMax == null) {
                        return 1;
                    } else {
                        return aMax - bMax;
                    }
                } else {
                    if (aMin == null) {
                        return bMin == null ? 0 : 1;
                    } else if (bMin == null) {
                        return -1;
                    } else {
                        return aMin - bMin;
                    }
                }
            }
        }
    }

    const filterCar = (car: CarModel): boolean => {
        if (nameFilter.trim() != '') {
            if (!car.make.toLowerCase().includes(nameFilter.toLowerCase().trim()) &&
                !car.model.toLowerCase().includes(nameFilter.toLowerCase().trim()) &&
                !car.variant.toLowerCase().includes(nameFilter.toLowerCase().trim())
            ) {
                return false;
            }
        }
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
                    mileage: [car.mileage, car.mileage],
                    power: [car.specs.enginePower, car.specs.enginePower],
                    torque: [car.specs.engineTorque, car.specs.engineTorque],
                    displacement: [car.specs.engineDisplacement, car.specs.engineDisplacement],
                    length: [car.specs.length, car.specs.length],
                    width: [car.specs.width, car.specs.width],
                    height: [car.specs.height, car.specs.height],
                    wheelbase: [car.specs.wheelbase, car.specs.wheelbase]
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
        group.mileage = computeNewMinMax(group.mileage, car.mileage)
        group.power = computeNewMinMax(group.power, car.power)
        group.torque = computeNewMinMax(group.torque, car.torque)
        group.displacement = computeNewMinMax(group.displacement, car.displacement)
        group.length = computeNewMinMax(group.length, car.length)
        group.width = computeNewMinMax(group.width, car.width)
        group.height = computeNewMinMax(group.height, car.height)
        group.wheelbase = computeNewMinMax(group.wheelbase, car.wheelbase)
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
                    mileage: car.mileage,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    length: car.length,
                    width: car.width,
                    height: car.height,
                    wheelbase: car.wheelbase,
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
                    mileage: car.mileage,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    length: car.length,
                    width: car.width,
                    height: car.height,
                    wheelbase: car.wheelbase,
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
                    mileage: car.mileage,
                    power: car.power,
                    torque: car.torque,
                    displacement: car.displacement,
                    length: car.length,
                    width: car.width,
                    height: car.height,
                    wheelbase: car.wheelbase,
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
    }, [allCars, nameFilter])

    const columns: TableColumnsType<TableCarData> = [
        {
            title: 'Name',
            key: 'name',
            fixed: true,
            render: (value, record) => {
                function getNumCars(record: TableCarData) {
                    const children: TableCarData[] | undefined = record.children;
                    if (children == null) {
                        return 1
                    }
                    let total = 0
                    children.forEach(child => {
                        total += getNumCars(child);
                    });
                    return total;
                }
                function groupDetails(record: TableCarData) {
                    if (isList() || record.children == null) {
                        return null;
                    }
                    const total = getNumCars(record)
                    return defaultTextRenderer(` (${total})`, true);
                }
                return <>
                    {(
                        record.url == null ? '' : <a href={record.url} target="_blank" rel="noopener noreferrer"><LinkOutlined /> </a>
                    )}
                    {defaultTextRenderer(record.name)}
                    {groupDetails(record)}
                </>
            },
            sorter: (a, b, sortOrder) => defaultTextSorter(a.name, b.name, sortOrder),
            defaultSortOrder: 'ascend',
            width: 420,
            ellipsis: true
        },
        {
            title: 'City',
            key: 'city',
            render: (value, record) => {
                return defaultTextRenderer(record.city ?? '')
            },
            width: 150
        },
        {
            title: 'Year',
            key: 'year',
            render: (value, record) => {
                return defaultRangeRenderer(record.year)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.year, b.year, sortOrder),
            width: 120
        },
        {
            title: 'Price',
            key: 'price',
            render: (value, record) => {
                return defaultRangeRenderer(record.price)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.price, b.price, sortOrder),
            width: 200
        },
        {
            title: 'Mileage',
            key: 'mileage',
            render: (value, record) => {
                return defaultRangeRenderer(record.mileage)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.mileage, b.mileage, sortOrder),
            width: 200
        },
        {
            title: 'Power',
            key: 'power',
            render: (value, record) => {
                return defaultRangeRenderer(record.power)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.power, b.power, sortOrder),
            width: 120
        },
        {
            title: 'Torque',
            key: 'torque',
            render: (value, record) => {
                return defaultRangeRenderer(record.torque)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.torque, b.torque, sortOrder),
            width: 120
        },
        {
            title: 'Displacement',
            key: 'displacement',
            render: (value, record) => {
                return defaultRangeRenderer(record.displacement)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.displacement, b.displacement, sortOrder),
            width: 120
        },
        {
            title: 'Length',
            key: 'length',
            render: (value, record) => {
                return defaultRangeRenderer(record.length)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.length, b.length, sortOrder),
            width: 120
        },
        {
            title: 'Width',
            key: 'width',
            render: (value, record) => {
                return defaultRangeRenderer(record.width)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.width, b.width, sortOrder),
            width: 120
        },
        {
            title: 'Height',
            key: 'height',
            render: (value, record) => {
                return defaultRangeRenderer(record.height)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.height, b.height, sortOrder),
            width: 120
        },
        {
            title: 'Wheelbase',
            key: 'wheelbase',
            render: (value, record) => {
                return defaultRangeRenderer(record.wheelbase)
            },
            sorter: (a, b, sortOrder) => defaultRangeSorter(a.wheelbase, b.wheelbase, sortOrder),
            width: 120
        },
    ]

    const allSelectableColumnKeys: Key[] = columns.filter((column) => { return column.fixed == null || column.fixed == false }).map((column) => { return column.key ?? '' })
    const [selectedColumnKeys, setSelectedColumnKeys] = useState<Key[]>(allSelectableColumnKeys)

    const handleColumnChange = (columnKey: Key, checked: boolean) => {
        const nextSelectedColumns = checked
            ? [...selectedColumnKeys, columnKey]
            : selectedColumnKeys.filter((c) => c !== columnKey);
        setSelectedColumnKeys(nextSelectedColumns);
    };

    const capitalize = (text: string): string => {
        return text.charAt(0).toUpperCase() + text.slice(1);
    }

    return <>
        <Flex vertical>
            <Flex align='center' justify='space-between' style={{ padding: '8px 0 8px 0' }}>
                <Input
                    placeholder={`Search ${allCars.length} cars`}
                    style={{ width: '25%' }}
                    value={nameFilter}
                    onChange={e => { setNameFilter(e.target.value) }} />
                <Flex wrap align="center">
                    {allSelectableColumnKeys.map<React.ReactNode>((columnKey) => (
                        <Tag.CheckableTag
                            key={columnKey}
                            checked={selectedColumnKeys.includes(columnKey)}
                            onChange={(checked) => handleColumnChange(columnKey, checked)}
                        >
                            {capitalize(columnKey.toLocaleString())}
                        </Tag.CheckableTag>
                    ))}
                </Flex>
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
            </Flex>

            <ConfigProvider theme={{
                token: {
                    colorBorderSecondary: 'rgba(127,127,127,0.0)',
                    borderRadiusLG: 0,
                },
                components: {
                    Table: {
                        headerBg: colorBgContainer,
                        headerSortActiveBg: isAscend ? colorSuccessBg : colorErrorBg,
                    }
                }
            }}>
                <Table<TableCarData>
                    columns={columns.filter(column => column.fixed || selectedColumnKeys.includes(column.key ?? ''))}
                    dataSource={isList() ? filteredCars : filteredCarsTree}
                    loading={loading}
                    virtual
                    bordered
                    scroll={{ x: '100%', y: '100%' }}
                    showSorterTooltip={false}
                    pagination={false}
                />
            </ConfigProvider>
        </Flex>
    </>
}

export default AllCarsList;

