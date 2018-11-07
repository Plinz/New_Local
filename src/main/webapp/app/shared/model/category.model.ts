import { ICategory } from 'app/shared/model//category.model';

export interface ICategory {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
    categoryParent?: ICategory;
}

export class Category implements ICategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public categoryParent?: ICategory
    ) {}
}
