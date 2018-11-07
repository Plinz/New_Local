import { IPerson } from 'app/shared/model//person.model';

export interface ILocation {
    id?: number;
    city?: string;
    country?: string;
    zip?: string;
    address?: string;
    lon?: number;
    lat?: number;
    person?: IPerson;
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public city?: string,
        public country?: string,
        public zip?: string,
        public address?: string,
        public lon?: number,
        public lat?: number,
        public person?: IPerson
    ) {}
}
